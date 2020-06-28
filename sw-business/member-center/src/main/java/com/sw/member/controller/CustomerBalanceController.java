package com.sw.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.customer.CustomerBalance;
import com.sw.common.entity.customer.CustomerBalanceDetail;
import com.sw.common.entity.customer.CustomerPoint;
import com.sw.common.util.*;
import com.sw.client.controller.BaseController;
import com.sw.member.service.impl.CustomerBalanceDetailServiceImpl;
import com.sw.member.service.impl.CustomerBalanceServiceImpl;
import com.sw.member.service.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("customerBalance")
public class CustomerBalanceController extends BaseController<CustomerBalanceServiceImpl,CustomerBalance> {

    @Autowired
    CustomerServiceImpl customerService;

    @Autowired
    CustomerBalanceServiceImpl customerBalanceService;

    @Autowired
    CustomerBalanceDetailServiceImpl customerBalanceDetailService;

    @RequestMapping(value = "/updateBalance",method = RequestMethod.POST)
    @ResponseBody
    public DataResponse updateObj(@RequestBody Map<String, Object> params){
        String openid = MapUtil.getMapValue(params, "openid");
        if(openid.equals(AppContext.getCurrentUserWechatOpenId())){
            String amountStr = MapUtil.getMapValue(params, "amount", "0");
            String customerId = MapUtil.getMapValue(params, "customerId", "0");
            String remark = MapUtil.getMapValue(params, "remark", "");
            BigDecimal amount = new BigDecimal(amountStr);
            if(StringUtil.isEmpty(openid)){
                return DataResponse.fail("用户不能为空");
            }

            QueryWrapper<CustomerBalance> customerBalanceEntityWrapper = new QueryWrapper<>();
            customerBalanceEntityWrapper.eq("IS_DELETE", 0);
            customerBalanceEntityWrapper.eq("FK_CUSTOMER_ID", customerId);
            CustomerBalance customerBalance = customerBalanceService.getOne(customerBalanceEntityWrapper);
            if(customerBalance == null){
                // 新增
                customerBalance = new CustomerBalance();
                customerBalance.setBalance(amount);
                customerBalance.setWithdrawCash(new BigDecimal("0"));
                customerBalance.setFkCustomerId(customerId);
                customerBalance.setIsDelete(0);
                customerBalance.setAddUser("微信");
                customerBalance.setAddTime(DateUtil.getCurrentDateTime());
                customerBalance.setUpdateUser("微信");
                customerBalance.setUpdateTime(DateUtil.getCurrentDateTime());
                customerBalanceService.save(customerBalance);
            }else{
                BigDecimal balance = customerBalance.getBalance();
                balance = balance.add(amount);
                customerBalance.setBalance(balance);
                customerBalance.setUpdateUser("微信");
                customerBalance.setUpdateTime(DateUtil.getCurrentDateTime());
                customerBalanceService.updateById(customerBalance);
            }

            // 新增余额明细
            CustomerBalanceDetail customerBalanceDetail = new CustomerBalanceDetail();
            customerBalanceDetail.setFkCustomerId(customerId);
            customerBalanceDetail.setBalance(amount);
            customerBalanceDetail.setType("SW1501");
            customerBalanceDetail.setRemark(remark);
            customerBalanceDetail.setStatus(StatusDict.START.getCode());
            customerBalanceDetail.setTime(DateUtil.getCurrentDateTime());
            customerBalanceDetail.setIsDelete(0);
            customerBalanceDetail.setAddUser("微信");
            customerBalanceDetail.setAddTime(DateUtil.getCurrentDateTime());
            customerBalanceDetail.setUpdateUser("微信");
            customerBalanceDetail.setUpdateTime(DateUtil.getCurrentDateTime());
            customerBalanceDetailService.save(customerBalanceDetail);
        }else{
            DataResponse.fail("当前登录用户不匹配");
        }

        return DataResponse.success();
    }

    /**
     * 获取余额
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBalance", method = RequestMethod.POST)
    public DataResponse getBalance(@RequestBody Map<String, Object> param){
        log.info("==============开始调用 getBalance================");

        Map<String, Object> result = new HashMap<>();

        String customerId = MapUtil.getMapValue(param, "customerId");

        QueryWrapper<CustomerBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("FK_CUSTOMER_ID", customerId);

        CustomerBalance customerBalance = customerBalanceService.getOne(wrapper);

        result.put("customerBalance", customerBalance);

        return DataResponse.success(result);
    }

    @ResponseBody
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public CustomerBalance selectOne(@RequestBody Map<String, Object> map) {
        QueryWrapper<CustomerBalance> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FK_CUSTOMER_ID", MapUtil.getString(map, "FK_CUSTOMER_ID"));
        return service.getOne(wrapper);
    }
}
