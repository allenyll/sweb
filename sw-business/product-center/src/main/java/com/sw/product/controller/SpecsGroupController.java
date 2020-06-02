package com.sw.product.controller;

import com.sw.common.entity.product.SpecsGroup;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.SpecsGroupServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("specsGroup")
public class SpecsGroupController extends BaseController<SpecsGroupServiceImpl, SpecsGroup> {

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<SpecsGroup> list = (List<SpecsGroup>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(SpecsGroup specsGroup:list){
                Map<String, String> _map = new HashMap<>();
                map.put(specsGroup.getPkSpecsGroupId(), specsGroup.getName());
                _map.put("label", specsGroup.getName());
                _map.put("value", specsGroup.getPkSpecsGroupId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

}
