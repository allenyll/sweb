package com.sw.client.fallback;

import com.sw.client.feign.OrderFeignClient;
import com.sw.common.entity.cms.SearchHistory;
import com.sw.common.entity.order.Order;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class OrderFallbackFactory implements FallbackFactory<OrderFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFallbackFactory.class);

    @Override
    public OrderFeignClient create(Throwable throwable) {
        return new OrderFeignClient() {
            @Override
            public Order selectById(String orderId) {
                LOGGER.error("FEIGN调用：根据订单ID获取订单失败");
                return null;
            }

            @Override
            public void updateById(Order order) {
                LOGGER.error("FEIGN调用：更新订单失败");
            }
        };
    }
}
