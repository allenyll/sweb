package com.sw.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.entity.product.SpecOption;
import com.sw.common.entity.product.Specs;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.SpecOptionServiceImpl;
import com.sw.product.service.impl.SpecsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("specs")
public class SpecsController extends BaseController<SpecsServiceImpl, Specs> {

    @Autowired
    SpecsServiceImpl specsService;

    @Autowired
    SpecOptionServiceImpl specOptionService;

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<Specs> list = (List<Specs>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(Specs specs:list){
                Map<String, String> _map = new HashMap<>();
                map.put(specs.getPkSpecsId(), specs.getSpecsName());
                _map.put("label", specs.getSpecsName());
                _map.put("value", specs.getPkSpecsId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

    @ResponseBody
    @RequestMapping(value = "getSpecsListCondition", method = RequestMethod.POST)
    public DataResponse getSpecsListCondition(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> list = new ArrayList<>();

        QueryWrapper<Specs> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.like("FK_CATEGORY_ID", MapUtil.getMapValue(params, "categoryId"));

        List<Specs> specsList = specsService.list(wrapper);
        if(CollectionUtil.isNotEmpty(specsList)) {
            for(Specs specs:specsList){
                QueryWrapper<SpecOption> entityWrapper = new QueryWrapper<>();
                entityWrapper.eq("IS_DELETE", 0);
                entityWrapper.eq("FK_SPECS_ID", specs.getPkSpecsId());
                List<SpecOption> specOptions = specOptionService.list(entityWrapper);
                if(CollectionUtil.isNotEmpty(specOptions)){
                    Map<String, Object> specOptionMap = new HashMap();
                    specOptionMap.put("specName", specs.getSpecsName());
                    specOptionMap.put("specId", specs.getPkSpecsId());
                    specOptionMap.put("specOptions", specOptions);
                    list.add(specOptionMap);
                }
            }
        }

        result.put("list", list);

        return DataResponse.success(result);
    }


    @Override
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable String id){
        Map<String, Object> result = new HashMap<>();

        DataResponse dataResponse = super.get(id);
        Specs obj = (Specs) dataResponse.get("obj");
        if(obj != null){
            String categoryId = obj.getFkCategoryId();
            categoryId = categoryId.substring(1, categoryId.length() - 1).replace("\"", "");
            String[] categoryIdArr = categoryId.split(",");
            obj.setCategoryIds(categoryIdArr);
        }

        result.put("obj", obj);


        return DataResponse.success(result);
    }

}
