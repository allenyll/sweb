package com.sw.client.feign;

import com.sw.client.fallback.CustomerFallbackFactory;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:  feign 会员服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.MEMBER_SERVICE, fallbackFactory = CustomerFallbackFactory.class, decode404 = true)
public interface CustomerFeignClient {

    @RequestMapping(value = "customer/selectOne", method = RequestMethod.POST)
    Customer selectOne(@RequestBody Map<String, Object> map);

    @RequestMapping(value = "customer/selectCustomerById", method = RequestMethod.POST)
    Customer selectCustomerById(@RequestParam String fkCustomerId);

    @RequestMapping(value = "customerAddress/selectAddressById", method = RequestMethod.POST)
    CustomerAddress selectAddressById(@RequestParam String fkAddressId);
}
