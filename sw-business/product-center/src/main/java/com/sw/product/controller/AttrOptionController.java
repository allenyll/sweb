package com.sw.product.controller;

import com.sw.common.entity.product.AttrOption;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.AttrOptionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("attrOption")
public class AttrOptionController extends BaseController<AttrOptionServiceImpl, AttrOption> {


}
