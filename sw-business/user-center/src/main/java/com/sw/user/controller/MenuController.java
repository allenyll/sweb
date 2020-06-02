package com.sw.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.user.Menu;
import com.sw.common.entity.user.MenuTree;
import com.sw.common.util.DataResponse;
import com.sw.common.util.TreeUtil;
import com.sw.client.controller.BaseController;
import com.sw.user.service.impl.MenuServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2018-06-12
 */
@Api(value = "菜单相关接口", tags = "菜单管理")
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController<MenuServiceImpl, Menu> {

    private static final Logger LOG = LoggerFactory.getLogger(com.sw.user.controller.MenuController.class);

    /**
     * 获取菜单
     * @param params
     * @return
     */
    @ApiOperation(value = "获取全部的菜单信息")
    @ResponseBody
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET)
    public DataResponse getAllMenu(@RequestParam Map<String, Object> params){
        LOG.info("==============开始调用getAllMenu================");

        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Menu> wrapper = super.mapToWrapper(params);
        List<Menu> menuList = service.list(wrapper);
        List<MenuTree> list = getMenuTree(menuList, BaseConstants.MENU_ROOT);
        result.put("menus", list);

        return DataResponse.success(result);
    }

    @ApiOperation(value = "根据菜单ID获取菜单")
    @Override
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable String id){
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("PK_MENU_ID", id);

        Menu sysMenu = service.getOne(wrapper);

        if("0".equals(id)){
            sysMenu = new Menu();
            sysMenu.setPkMenuId(id);
            sysMenu.setMenuName("顶级节点");
        }else{
            setParentMenu(sysMenu);
        }
        result.put("obj", sysMenu);

        return DataResponse.success(result);
    }

    @ApiOperation(value = "组装菜单树")
    @ResponseBody
    @RequestMapping(value = "getMenuTree", method = RequestMethod.GET)
    public DataResponse getMenuTree(String type){
        LOG.info("==================开始调用getMenuTree================");
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        if ("menu".equals(type)) {
            wrapper.eq("MENU_TYPE", "SW0101");
        }

        List<Menu> menuList = service.list(wrapper);
        if(!CollectionUtils.isEmpty(menuList)){
            for(Menu menu:menuList){
                setParentMenu(menu);
            }
        }
        Menu topMenu = new Menu();
        topMenu.setPkMenuId("0");
        topMenu.setIsDelete(0);
        topMenu.setMenuName("顶级节点");
        topMenu.setMenuCode("top");
        topMenu.setParentMenuId("top");
        topMenu.setMenuIcon("sw-top");
        menuList.add(topMenu);
        List<MenuTree> menuTrees = getMenuTree(menuList, "top");
        result.put("menuTree", menuTrees);
        LOG.info("==================结束调用getMenuTree================");
        return DataResponse.success(result);
    }

    private List<MenuTree> getMenuTree(List<Menu> menuList, String menuRootId) {
        List<MenuTree> menuTrees = new ArrayList<>();
        MenuTree menuTree;
        if(!CollectionUtils.isEmpty(menuList)){
            for(Menu menu:menuList){
                menuTree = new MenuTree();
                menuTree.setId(menu.getPkMenuId());
                menuTree.setParentId(menu.getParentMenuId());
                menuTree.setCode(menu.getMenuCode());
                menuTree.setName(menu.getMenuName());
                menuTree.setHref(menu.getMenuUrl());
                menuTree.setTitle(menu.getMenuName());
                menuTree.setLabel(menu.getMenuName());
                menuTree.setIcon(menu.getMenuIcon());
                menuTrees.add(menuTree);
            }
        }
        return TreeUtil.build(menuTrees, menuRootId);
    }

    private void setParentMenu(Menu sysMenu) {
        String parentId = sysMenu.getParentMenuId();
        if(parentId.equals(BaseConstants.MENU_ROOT)){
            sysMenu.setParentMenuName("顶级节点");
        }else{
            QueryWrapper<Menu> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("IS_DELETE", 0);
            entityWrapper.eq("PK_MENU_ID", parentId);
            Menu menu = service.getOne(entityWrapper);
            if(menu != null){
                sysMenu.setParentMenuName(menu.getMenuName());
            }
        }
    }

}
