package com.sw.member.controller;

import com.sw.common.entity.customer.CustomerLevel;
import com.sw.client.controller.BaseController;
import com.sw.member.service.impl.CustomerLevelServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("customerLevel")
public class CustomerLevelController extends BaseController<CustomerLevelServiceImpl,CustomerLevel> {

}
