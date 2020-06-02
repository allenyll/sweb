package com.sw.client.fallback;

import com.sw.client.feign.MarketFeignClient;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class MarketFallbackFactory implements FallbackFactory<MarketFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketFallbackFactory.class);

    @Override
    public MarketFeignClient create(Throwable throwable) {
        return new MarketFeignClient() {
            @Override
            public List<Coupon> getCouponList(Map<String, Object> param) {
                LOGGER.error("获取优惠券列表失败");
                return null;
            }

            @Override
            public List<CouponDetail> getCouponDetailList(Map<String, Object> param) {
                LOGGER.error("获取优惠券详情失败");
                return null;
            }

            @Override
            public void updateById(CouponDetail couponDetail) {
                LOGGER.error("更新优惠券失败");
            }
        };
    }
}
