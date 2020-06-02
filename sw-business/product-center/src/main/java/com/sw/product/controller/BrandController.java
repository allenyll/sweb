package com.sw.product.controller;

import com.sw.common.entity.product.Brand;
import com.sw.common.util.DataResponse;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.BrandServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("品牌接口")
@Controller
@RequestMapping("brand")
public class BrandController extends BaseController<BrandServiceImpl, Brand> {

    @ApiOperation("品牌列表")
    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<Brand> list = (List<Brand>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(Brand brand:list){
                Map<String, String> _map = new HashMap<>();
                map.put(brand.getPkBrandId(), brand.getBrandName());
                _map.put("label", brand.getBrandName());
                _map.put("value", brand.getPkBrandId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

}
