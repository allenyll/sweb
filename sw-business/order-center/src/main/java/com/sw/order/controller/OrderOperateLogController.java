package com.sw.order.controller;

import com.sw.common.entity.order.OrderOperateLog;
import com.sw.client.controller.BaseController;
import com.sw.order.service.impl.OrderOperateLogServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orderOperateLog")
public class OrderOperateLogController extends BaseController<OrderOperateLogServiceImpl,OrderOperateLog> {


}
