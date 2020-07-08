package com.sw.market.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.market.Ad;
import com.sw.common.util.DataResponse;
import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.client.controller.BaseController;
import com.sw.market.service.impl.AdServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("广告接口")
@Controller
@RequestMapping("ad")
public class AdController extends BaseController<AdServiceImpl,Ad> {

    @Autowired
    AdServiceImpl adService;

    @ApiOperation("广告列表(前台数据展示转换)")
    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        List<Ad> list = (List<Ad>) data.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(Ad Ad:list){
                Map<String, String> _map = new HashMap<>();
                map.put(Ad.getPkAdId(), Ad.getAdName());
                _map.put("label", Ad.getAdName());
                _map.put("value", Ad.getPkAdId());
                newList.add(_map);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("map", map);
        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation("广告列表")
    @ResponseBody
    @RequestMapping(value = "getAdList", method = RequestMethod.POST)
    public DataResponse getAdList(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String adType = MapUtil.getString(params, "adType");
        String time = DateUtil.getCurrentDateTime();
        QueryWrapper<Ad> wrapper = new QueryWrapper<>();
        wrapper.eq("AD_TYPE", adType);
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("IS_USED", StatusDict.START.getCode());
        wrapper.gt("END_TIME", time);
        wrapper.lt("START_TIME", time);
        List<Ad> ads = adService.list(wrapper);
        result.put("adList", ads);
        return DataResponse.success(result);
    }

}
