package com.sw.member.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.entity.customer.Customer;
import com.sw.common.util.MapUtil;
import com.sw.client.controller.BaseController;
import com.sw.member.service.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController<CustomerServiceImpl, Customer> {

    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public Customer selectOne(@RequestBody Map<String, Object> map) {
        QueryWrapper<Customer> customerEntityWrapper = new QueryWrapper<>();
        customerEntityWrapper.eq("IS_DELETE", 0);
        customerEntityWrapper.eq("PK_CUSTOMER_ID", MapUtil.getString(map, "PK_CUSTOMER_ID"));
        return service.getOne(customerEntityWrapper);
    }

    @RequestMapping(value = "/selectCustomerById", method = RequestMethod.POST)
    public Customer selectCustomerById(@RequestParam String fkCustomerId) {
        return service.getById(fkCustomerId);
    }

}
