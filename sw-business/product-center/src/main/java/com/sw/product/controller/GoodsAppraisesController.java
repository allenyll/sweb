package com.sw.product.controller;

import com.sw.common.entity.product.GoodsAppraises;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.GoodsAppraisesServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("oodsAppraises")
public class GoodsAppraisesController extends BaseController<GoodsAppraisesServiceImpl, GoodsAppraises> {


}
