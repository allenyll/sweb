package com.sw.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.entity.order.Order;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.order.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description:  多线程处理订单
 * @Author:       allenyll
 * @Date:         2020-03-03 18:13
 * @Version:      1.0
 */
@Controller
@RequestMapping("/system-web/orderDeal")
public class OrderDealController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDealController.class);

    @Autowired
    OrderServiceImpl orderService;

    private static BlockingQueue<Order> orderQueue = new LinkedBlockingDeque<>();
    private static BlockingQueue<Order> orderQueue2 = new LinkedBlockingDeque<>();

    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    @ResponseBody
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public DataResponse orderDeal(){
        QueryWrapper<Order> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("IS_DELETE", 0);

        List<Order> list = orderService.list(entityWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            for (Order order:list) {
                orderQueue.offer(order);
                orderQueue2.offer(order);
            }
            long sTime1 = System.currentTimeMillis();
            orderDealDetail(list);
            long eTime1 = System.currentTimeMillis();

            System.out.println("多线程执行耗时：" + (eTime1 - sTime1));

            long sTime2 = System.currentTimeMillis();
            orderDetail(list);
            long eTime2 = System.currentTimeMillis();
            System.out.println("耗时2：" + (eTime2 - sTime2));

        }


        return DataResponse.success();
    }

    private String orderDetail(List<Order> list) {
        while (true) {
            if (orderQueue2.size() == 0) {
                LOGGER.info("操作完毕");
                return "success";
            }
            try {
                Order order = orderQueue2.take();
                LOGGER.info("orderDetail：" + order.getOrderNo());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

    private void orderDealDetail(List<Order> list) {
        List<Future<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Future<String> future = executor.submit(new OrderDetailThread());
            futureList.add(future);
        }

//        for (Future<String> future:futureList) {
//            try {
//                while (!future.isDone()) {
//                    LOGGER.info("Future返回如果没有完成，则一直循环等待，直到Future返回完成");
//                }
//                String result = future.get();
//                if ("success".equals(result)) {
//                    LOGGER.info("操作成功");
//                } else if ("fail".equals(result)) {
//                    LOGGER.info("操作失败");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }

    }

    class OrderDetailThread implements Callable {

        @Override
        public Object call() throws Exception {
            // 使用一个为真while循环保证
            while (true) {
                // 队列是否有数据
                if (orderQueue.size() == 0) {
                    LOGGER.info("操作完毕");
                    return "success";
                }

                Order order = orderQueue.take();
                LOGGER.info(order.getOrderNo());
                return "success";
            }
        }
    }


}
