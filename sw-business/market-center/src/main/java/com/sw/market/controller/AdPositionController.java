package com.sw.market.controller;

import com.sw.common.entity.market.AdPosition;
import com.sw.common.util.DataResponse;
import com.sw.client.controller.BaseController;
import com.sw.market.service.impl.AdPositionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("adPosition")
public class AdPositionController extends BaseController<AdPositionServiceImpl,AdPosition> {

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<AdPosition> list = (List<AdPosition>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(AdPosition adPosition:list){
                Map<String, String> _map = new HashMap<>();
                map.put(adPosition.getPkAdPositionId(), adPosition.getName());
                _map.put("label", adPosition.getName());
                _map.put("value", adPosition.getPkAdPositionId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

}
