package com.sw.order.consumer;

import com.sw.order.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    IOrderService orderService;

    @RabbitListener(queues = "sw.order.cancel")
    public void handle(String orderId) {
        LOGGER.info("receive delay message orderId:{}", orderId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("note", "支付超时自动取消");
        orderService.cancelOrder(map);
    }
}
