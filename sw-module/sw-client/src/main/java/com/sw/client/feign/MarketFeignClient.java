package com.sw.client.feign;

import com.sw.client.fallback.MarketFallbackFactory;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @Description:  feign 会员服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.MARKET_SERVICE, fallbackFactory = MarketFallbackFactory.class, decode404 = true)
public interface MarketFeignClient {

    @RequestMapping(value = "coupon/getCoupons", method = RequestMethod.POST)
    List<Coupon> getCouponList(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "couponDetail/getCouponDetailList", method = RequestMethod.POST)
    List<CouponDetail> getCouponDetailList(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "couponDetail/updateById", method = RequestMethod.POST)
    void updateById(@RequestBody CouponDetail couponDetail);
}
