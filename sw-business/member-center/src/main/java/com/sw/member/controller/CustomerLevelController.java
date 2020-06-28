package com.sw.member.controller;

import com.sw.common.entity.customer.CustomerLevel;
import com.sw.client.controller.BaseController;
import com.sw.member.service.impl.CustomerLevelServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customerLevel")
public class CustomerLevelController extends BaseController<CustomerLevelServiceImpl,CustomerLevel> {

}
