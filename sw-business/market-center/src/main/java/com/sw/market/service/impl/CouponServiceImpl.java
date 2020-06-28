package com.sw.market.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.client.feign.CustomerFeignClient;
import com.sw.common.constants.dict.CouponDict;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import com.sw.common.util.*;
import com.sw.market.mapper.CouponMapper;
import com.sw.market.service.ICouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * snu_coupon
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-28 09:13:54
 */
@Service("couponService")
public class CouponServiceImpl extends ServiceImpl<CouponMapper,Coupon> implements ICouponService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @Autowired
    CouponDetailServiceImpl couponDetailService;

    public void addCouponGoods(Map<String, Object> map) {
        couponMapper.addCouponGoods(map);
    }

    public List<Map<String, Object>> selectCouponGoods(String pkCouponId) {
        return couponMapper.selectCouponGoods(pkCouponId);
    }

    public DataResponse publishCoupon(Map<String, Object> params) {
        String customerId = MapUtil.getString(params, "customerId");
        if (StringUtil.isEmpty(customerId)){
            return DataResponse.fail("发放用户不能为空，请选择！");
        }
        String couponId = MapUtil.getString(params, "couponId");
        if (StringUtil.isEmpty(couponId)) {
            return DataResponse.fail("发放优惠券不能为空，请选择！");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("PK_CUSTOMER_ID", customerId);
        map.put("MARK", "user");
        Customer customer = customerFeignClient.selectOne(map);
        if (customer == null) {
            return DataResponse.fail("用户不存在，请选择！");
        }

        QueryWrapper<Coupon> couponEntityWrapper = new QueryWrapper<>();
        couponEntityWrapper.eq("IS_DELETE", 0);
        couponEntityWrapper.eq("PK_COUPON_ID", couponId);
        List<Coupon> coupon = couponMapper.selectList(couponEntityWrapper);
        if (CollectionUtil.isEmpty(coupon)) {
            return DataResponse.fail("优惠券已失效，请选择！");
        }

        CouponDetail couponDetail = new CouponDetail();
        couponDetail.setCouponCode(coupon.get(0).getCode());
        couponDetail.setNickName(customer.getNickName());
        couponDetail.setFkCouponId(couponId);
        couponDetail.setFkCustomerId(customerId);
        couponDetail.setGetType(CouponDict.BACKSTAGE_GIFT.getCode());
        couponDetail.setUseStatus(CouponDict.UN_USE.getCode());
        couponDetail.setIsDelete(0);
        couponDetail.setAddTime(DateUtil.getCurrentDateTime());

        try {
            couponDetailService.save(couponDetail);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.fail(e.getMessage());
        }

        return DataResponse.success();
    }

    public DataResponse getCouponList(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String customerId = MapUtil.getString(params, "customerId");
        if (StringUtil.isEmpty(customerId)){
            return DataResponse.fail("用户不能为空，请选择！");
        }

        params.put("time", DateUtil.getCurrentDate());

        List<Map<String, Object>> list = couponMapper.getCouponList(params);

        result.put("list", list);

        return DataResponse.success(result);
    }
}
