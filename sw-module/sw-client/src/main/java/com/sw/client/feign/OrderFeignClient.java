package com.sw.client.feign;

import com.sw.client.fallback.OrderFallbackFactory;
import com.sw.client.fallback.UserFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.cms.SearchHistory;
import com.sw.common.entity.order.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:  内容管理开放接口
 * @Author:       allenyll
 * @Date:         2020/5/10 11:50 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.ORDER_SERVICE, fallbackFactory = OrderFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface OrderFeignClient {

    @RequestMapping(value = "order/selectById", method = RequestMethod.POST)
    Order selectById(@RequestParam String orderId);

    @RequestMapping(value = "order/updateById", method = RequestMethod.POST)
    void updateById(@RequestBody Order order);
}
