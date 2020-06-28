package com.sw.cms.controller;

import com.sw.client.controller.BaseController;
import com.sw.cms.service.impl.SearchHistoryServiceImpl;
import com.sw.common.entity.cms.SearchHistory;
import com.sw.common.util.DataResponse;
import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Api(value = "搜索记录接口", tags = "搜索记录模块")
@Controller
@RequestMapping("searchHistory")
public class SearchHistoryController extends BaseController<SearchHistoryServiceImpl, SearchHistory> {

    @Autowired
    SearchHistoryServiceImpl searchHistoryService;

    @ApiOperation("清除搜索记录")
    @ResponseBody
    @RequestMapping(value = "clearHistoryKeyword", method = RequestMethod.POST)
    public DataResponse clearHistoryKeyword(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashedMap();
        String customerId = MapUtil.getString(params, "userId");
        if (StringUtil.isEmpty(customerId)) {
            return DataResponse.fail("关联用户为空，无法查询");
        }
        params.put("time", DateUtil.getCurrentDateTime());

        int num = searchHistoryService.updateByCustomerId(params);

        return DataResponse.success(result);
    }

    @ApiOperation("新增搜索记录")
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public void insert(@RequestBody SearchHistory searchHistory) {
        service.save(searchHistory);
    }

}
