package com.sw.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.controller.BaseController;
import com.sw.cms.service.IKeyWordsService;
import com.sw.cms.service.ISearchHistoryService;
import com.sw.cms.service.impl.KeywordsServiceImpl;
import com.sw.common.entity.cms.Keywords;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "关键字管理", tags = "关键字模块")
@Slf4j
@Controller
@RequestMapping("keywords")
public class KeywordsController extends BaseController<KeywordsServiceImpl, Keywords> {


    @Autowired
    IKeyWordsService keywordsService;

    @Autowired
    ISearchHistoryService searchHistoryService;

    @ApiOperation("获取关键词")
    @ResponseBody
    @RequestMapping(value = "getSearchKeyword", method = RequestMethod.POST)
    public DataResponse getSearchKeyword(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashedMap();
        QueryWrapper<Keywords> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("IS_DEFAULT", "SW1001");
        List<Keywords> isDefaultKeywords = keywordsService.list(wrapper);
        Keywords keywords = new Keywords();
        if (CollectionUtil.isNotEmpty(isDefaultKeywords)) {
            keywords = isDefaultKeywords.get(0);
        }

        List<Map<String, String>> isHotKeyWords = keywordsService.selectHotKeywordList();

        result.put("isDefaultKeyWords", keywords);
        result.put("isHotKeywords", isHotKeyWords);

        String customerId = MapUtil.getString(params, "userId");
        List<Map<String, String>> historyKeywords = null;

        if (StringUtil.isEmpty(customerId)) {
            historyKeywords = new ArrayList<>();
            result.put("historyKeywords", historyKeywords);
            return DataResponse.success(result);
        }

        historyKeywords = searchHistoryService.selectHistoryKeywordList(params);

        result.put("historyKeywords", historyKeywords);

        return DataResponse.success(result);
    }

    @ApiOperation("获取关键词列表")
    @ResponseBody
    @RequestMapping(value = "getKeywords", method = RequestMethod.POST)
    public DataResponse getKeywords(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashedMap();
        String keyword = MapUtil.getString(params, "keyword");
        if (StringUtil.isEmpty(keyword)) {
            return DataResponse.fail("关键字为空，无法查询");
        }
        QueryWrapper<Keywords> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.like("KEYWORD", keyword);
        List<Keywords> keywords = keywordsService.list(wrapper);
        if (keywords != null && keywords.size() > 10) {
            keywords = keywords.subList(0, 10);
        }

        result.put("keywordList", keywords);

        return DataResponse.success(result);
    }

}
