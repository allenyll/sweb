package com.sw.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.user.Depot;
import com.sw.common.entity.user.DepotTree;
import com.sw.common.util.DataResponse;
import com.sw.common.util.StringUtil;
import com.sw.common.util.TreeUtil;
import com.sw.client.controller.BaseController;
import com.sw.user.service.impl.DepotServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理部门 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-20
 */
@Api(value = "组织相关接口", tags = "组织管理")
@Controller
@RequestMapping("/depot/")
public class DepotController extends BaseController<DepotServiceImpl, Depot> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepotController.class);

    @ApiOperation(value = "部门列表")
    @ResponseBody
    @RequestMapping(value = "getAllDepot", method = RequestMethod.GET)
    public DataResponse getDepotList(String name){
        LOGGER.info("============= {开始调用方法：getDepotList(} =============");
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Depot> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        if(StringUtil.isNotEmpty(name)){
            wrapper.like("DEPOT_NAME", name);
        }
        List<Depot> depots = service.list(wrapper);

        List<DepotTree> list = getDepotTree(depots, BaseConstants.MENU_ROOT);

        result.put("depots", list);
        LOGGER.info("============= {结束调用方法：getDepotList(} =============");
        return DataResponse.success(result);
    }

    @ApiOperation(value = "获取所有部门--树形结构")
    @ResponseBody
    @RequestMapping(value = "getDepotTree", method = RequestMethod.GET)
    public DataResponse getDepotTree(){
        LOGGER.info("==================开始调用getDepotTree================");
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Depot> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);

        List<Depot> list = service.list(wrapper);
        if(!CollectionUtils.isEmpty(list)){
            for(Depot depot:list){
                setParentDepot(depot);
            }
        }

        Depot depot = new Depot();
        depot.setPkDepotId("0");
        depot.setDepotName("顶级节点");
        depot.setDepotCode("top");
        depot.setDepotCode("top");
        depot.setIsDelete(0);
        depot.setParentDepotId("top");
        list.add(depot);

        List<DepotTree> trees = getDepotTree(list, "top");

        result.put("depotTree", trees);
        LOGGER.info("==================结束调用getDepotTree================");
        return DataResponse.success(result);
    }

    @ApiOperation(value = "根据部门ID获取具体部门")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable String id){
        LOGGER.info("==================开始调用 get================");
        DataResponse dataResponse = super.get(id);
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Depot depot = (Depot) data.get("obj");

        if(BaseConstants.MENU_ROOT.equals(id)){
            depot = new Depot();
            depot.setPkDepotId(id);
            depot.setDepotName("顶级节点");
        }else{
            setParentDepot(depot);
        }

        data.put("obj", depot);
        dataResponse.put("data", data);
        LOGGER.info("==================结束调用 get================");

        return dataResponse;
    }

    private List<DepotTree> getDepotTree(List<Depot> list, String rootId) {
        List<DepotTree> trees = new ArrayList<>();
        DepotTree tree;
        if(!CollectionUtils.isEmpty(list)){
            for(Depot obj:list){
                tree = new DepotTree();
                tree.setId(obj.getPkDepotId());
                tree.setParentId(obj.getParentDepotId());
                tree.setName(obj.getDepotName());
                tree.setCode(obj.getDepotCode());
                tree.setTitle(obj.getDepotName());
                tree.setLabel(obj.getDepotName());
                trees.add(tree);
            }
        }
        return TreeUtil.build(trees, rootId);
    }

    private void setParentDepot(Depot depot) {

        String parentId = depot.getParentDepotId();
        if(parentId.equals(BaseConstants.MENU_ROOT)){
            depot.setParentDepotName("顶级节点");
        }else{
            QueryWrapper<Depot> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("IS_DELETE", 0);
            entityWrapper.eq("PK_DEPOT_ID", parentId);
            Depot sysDepot = service.getOne(entityWrapper);
            if(sysDepot != null){
                depot.setParentDepotName(sysDepot.getDepotName());
            }
        }
    }


}
