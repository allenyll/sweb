package com.sw.client.feign;

import com.sw.client.fallback.UserFallbackFactory;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.cms.SearchHistory;
import com.sw.common.entity.user.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:  内容管理开放接口
 * @Author:       allenyll
 * @Date:         2020/5/10 11:50 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.CMS_SERVICE, fallbackFactory = UserFallbackFactory.class, decode404 = true)
public interface CmsFeignClient {

    /**
     * @param searchHistory
     * @return
     */
    @RequestMapping(value = "searchHistory/insert", method = RequestMethod.POST)
    void insert(@RequestBody SearchHistory searchHistory);

}
