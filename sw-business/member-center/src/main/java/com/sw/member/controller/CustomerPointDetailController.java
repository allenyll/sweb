package com.sw.member.controller;


import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.client.controller.BaseController;
import com.sw.member.service.impl.CustomerPointDetailServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@Controller
@RequestMapping("customerPointDetail")
public class CustomerPointDetailController extends BaseController<CustomerPointDetailServiceImpl, CustomerPointDetail> {

}
