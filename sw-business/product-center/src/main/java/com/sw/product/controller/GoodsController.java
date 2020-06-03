package com.sw.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.FileFeignClient;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.dict.IsOrNoDict;
import com.sw.common.entity.product.Goods;
import com.sw.common.entity.product.GoodsParam;
import com.sw.common.entity.user.File;
import com.sw.common.entity.user.User;
import com.sw.common.util.*;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.GoodsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("商品管理相关接口")
@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController<GoodsServiceImpl, Goods> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    FileFeignClient fileFeignClient;

    @Autowired
    GoodsServiceImpl goodsService;

//    @Autowired
//    SearchHistoryServiceImpl searchHistoryService;

    @ApiOperation("获取商品列表（前端展示使用）")
    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<Goods> list = (List<Goods>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            for(Goods goods:list){
                Map<String, String> _map = new HashMap<>();
                map.put(goods.getPkGoodsId(), goods.getGoodsName());
                _map.put("label", goods.getGoodsName());
                _map.put("value", goods.getPkGoodsId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

    @ApiOperation("获取商品列表")
    @ResponseBody
    @RequestMapping(value = "getGoodsList", method = RequestMethod.POST)
    public DataResponse getGoodsList(@RequestBody Map<String, Object> params) {
        String keyword = MapUtil.getString(params, "keyword");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        if(StringUtil.isNotEmpty(keyword)){
            wrapper.and(_wrapper -> wrapper.like("GOODS_NAME", keyword).or().like("GOODS_CODE", keyword));
        }
        List<Goods> list = goodsService.list(wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return DataResponse.success(result);
    }

    @ApiOperation("根据商品类型获取商品列表")
    @ResponseBody
    @RequestMapping(value = "getGoodsListByType", method = RequestMethod.POST)
    public DataResponse getGoodsListByType(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        String goodsType = MapUtil.getString(params, "goodsType");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("IS_USED", "SW1302");
        if (StringUtil.isEmpty(goodsType)) {
        result.put("goodsList", new ArrayList<>());
         return DataResponse.success(result);
        }
        if ("new".equals(goodsType)) {
            wrapper.eq("IS_NEW", IsOrNoDict.YES.getCode());
        } else if ("hot".equals(goodsType)) {
            wrapper.eq("IS_HOT", IsOrNoDict.YES.getCode());
        } else if ("recommend".equals(goodsType)) {
            wrapper.eq("IS_RECOM", IsOrNoDict.YES.getCode());
        } else if ("best".equals(goodsType)) {
            wrapper.eq("IS_BEST", IsOrNoDict.YES.getCode());
        }
        List<Goods> list = goodsService.list(wrapper);
        if(CollectionUtil.isNotEmpty(list)){
            for(Goods goods:list){
                setFile(goods);
            }
        }
        result.put("goodsList", list);
        return DataResponse.success(result);
    }

    @ApiOperation("分页查询商品")
    @Override
    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        LOGGER.info("传入参数=============" + params);
        DataResponse dataResponse = super.page(params);
        List<Goods> goodsList = (List<Goods>) dataResponse.get("list");
        if(CollectionUtil.isNotEmpty(goodsList)){
            for(Goods goods:goodsList){
                setFile(goods);
            }
        }
        result.put("total", dataResponse.get("total"));
        result.put("list", goodsList);
        return DataResponse.success(result);
    }

    @ApiOperation("创建商品")
    @ResponseBody
    @RequestMapping(value = "/createGoods", method = RequestMethod.POST)
    public DataResponse createGoods(@CurrentUser(isFull = true) User user, @RequestBody GoodsParam goodsParam) {
        LOGGER.debug("保存参数：{}", goodsParam);
        Map<String, Object> result = new HashMap<>();

        Goods goods = goodsParam;

        DataResponse dataResponse = super.add(user, goods);
        // 商品添加失败
        if(dataResponse.get("code").equals(BaseConstants.FAIL)){
            return dataResponse;
        }

        try {
            int count = goodsService.createGoods(goodsParam);
            result.put("count", count);
        } catch (Exception e) {
            LOGGER.error("创建商品失败");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("更新商品")
    @ResponseBody
    @RequestMapping(value = "/updateGoods/{id}", method = RequestMethod.POST)
    public DataResponse updateGoods(@CurrentUser(isFull = true) User user,@PathVariable String id, @RequestBody GoodsParam goodsParam) {
        LOGGER.debug("更新参数：{}", goodsParam);
        Map<String, Object> result = new HashMap<>();

        Goods goods = goodsParam;

        DataResponse dataResponse = super.update(user, goods);
        // 商品更新失败
        if(dataResponse.get("code").equals(BaseConstants.FAIL)){
            return dataResponse;
        }

        try {
            int count = goodsService.updateGoods(goodsParam);
            result.put("count", count);
        } catch (Exception e) {
            LOGGER.error("创建商品失败");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    /**
     * 更新商品状态
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateLabel", method = RequestMethod.POST)
    public DataResponse updateLabel(@CurrentUser(isFull = true) User user,@RequestBody Map<String, Object> params) {
        LOGGER.debug("保存参数：{}", params);
        Map<String, Object> result = new HashMap<>();
        String goodsId = MapUtil.getString(params, "id");
        String label = MapUtil.getString(params, "label");
        String status = MapUtil.getString(params, "status");

        DataResponse dataResponse = super.get(goodsId);

        Goods goods = (Goods) dataResponse.get("obj");

        if(goods == null){
            return DataResponse.fail("更新失败, 商品不存在");
        }
        if("isUsed".equals(label)){
            goods.setIsUsed(status);
        }else if("isRecom".equals(label)){
            goods.setIsRecom(status);
        }else if("isSpec".equals(label)){
            goods.setIsSpec(status);
        }else if("isBest".equals(label)){
            goods.setIsBest(status);
        }else if("isHot".equals(label)){
            goods.setIsHot(status);
        }else if("isNew".equals(label)){
            goods.setIsNew(status);
        }

        String userId = user.getPkUserId();

        goods.setUpdateTime(DateUtil.getCurrentDateTime());
        goods.setUpdateUser(userId);
        boolean flag = goodsService.updateById(goods);

        if(!flag){
            return DataResponse.fail("更新商品状态失败");
        }

        return DataResponse.success(result);
    }

    @ApiOperation("根据ID获取商品")
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable String id) {

        Map<String, Object> result = null;

        DataResponse dataResponse = super.get(id);
        Goods goods = (Goods) dataResponse.get("obj");

        if(goods == null) {
            return  DataResponse.fail("获取商品失败");
        }
        setFile(goods);
        try {
            result = goodsService.getGoodsInfo(goods);
        } catch (Exception e) {
            LOGGER.error("赋值异常");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("根据分类获取商品")
    @ResponseBody
    @RequestMapping(value = "/getGoods", method = RequestMethod.POST)
    public DataResponse getGoods(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        page = MapUtil.getIntValue(params, "page");
        limit = MapUtil.getIntValue(params, "limit");
        String id = MapUtil.getMapValue(params, "categoryId");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FK_CATEGORY_ID", id);

        int total = goodsService.count(wrapper);
        Page<Goods> pages = service.page(new Page<>(page, limit), wrapper);
        List<Goods> list = pages.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            for (Goods goods: list){
               setFile(goods);
            }
        }

        if(total%limit == 0){
            totalPage = total/limit;
        }else{
            totalPage = total/limit + 1;
        }

        result.put("currentPage", page);
        result.put("totalPage", totalPage);
        result.put("goods", list);

        return DataResponse.success(result);
    }

    @ApiOperation("获取商品详情")
    @ResponseBody
    @RequestMapping(value = "/getGoodsInfo/{id}", method = RequestMethod.POST)
    public DataResponse getGoodsInfo(@PathVariable String id){
        Map<String, Object> result = new HashMap<>();
        DataResponse data = super.get(id);

        Goods goods = (Goods) data.get("obj");
        if(goods == null){
            return DataResponse.fail("商品不存在");
        }

        setFile(goods);

        try {
            result = goodsService.getGoodsInfo(goods);
        } catch (Exception e) {
            LOGGER.error("赋值异常");
            e.printStackTrace();
        }

        return DataResponse.success(result);
    }

    @ApiOperation("查询商品")
    @ResponseBody
    @RequestMapping(value = "/searchGoods", method = RequestMethod.POST)
    public DataResponse searchGoods(@RequestBody Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();

        page = MapUtil.getIntValue(params, "page");
        limit = MapUtil.getIntValue(params, "limit");
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        String sort = MapUtil.getString(params, "sort");
        String order = MapUtil.getString(params, "order");
        boolean isAsc = true;
        if ("asc".endsWith(order)) {
            isAsc = true;
        } else {
            isAsc = false;
        }
        wrapper.orderBy(true, isAsc, sort);
        String keyword = MapUtil.getString(params, "keyword");
        if (StringUtil.isNotEmpty(keyword)) {
            String time = DateUtil.getCurrentDateTime();
            // 新增搜索记录
            String customerId = MapUtil.getString(params, "userId");
            if (StringUtil.isEmpty(customerId)) {
                return DataResponse.fail("关联用户为空，无法查询");
            }
//            SearchHistory searchHistory = new SearchHistory();
//            searchHistory.setFrom("小程序");
//            searchHistory.setKeyword(keyword);
//            searchHistory.setUserId(customerId);
//            searchHistory.setIsDelete(0);
//            searchHistory.setAddTime(time);
//            searchHistory.setAddUser(customerId);
//            searchHistory.setUpdateUser(customerId);
//            searchHistory.setUpdateTime(time);
//            searchHistoryService.insert(searchHistory);
        }
        wrapper.like("KEYWORDS", keyword);
        String categoryId = MapUtil.getMapValue(params, "categoryId");
        if (StringUtil.isNotEmpty(categoryId)) {
            wrapper.eq("FK_CATEGORY_ID", categoryId);
        }

        int total = goodsService.count(wrapper);
        Page<Goods> pages = service.page(new Page<>(page, limit), wrapper);
        List<Goods> list = pages.getRecords();
        if(CollectionUtil.isNotEmpty(list)){
            for (Goods goods: list){
                setFile(goods);
            }
        }

        if(total%limit == 0){
            totalPage = total/limit;
        }else{
            totalPage = total/limit + 1;
        }

        result.put("currentPage", page);
        result.put("totalPage", totalPage);
        result.put("goods", list);

        return DataResponse.success(result);
    }

    private void setFile(Goods goods) {
        File file = new File();
        file.setFileType("SW1801");
        file.setIsDelete(0);
        file.setFkId(goods.getPkGoodsId());
        List<File> sysFiles = fileFeignClient.list(file);
        if(CollectionUtil.isNotEmpty(sysFiles)){
            goods.setFileList(sysFiles);
            goods.setFileUrl(sysFiles.get(0).getFileUrl());
        }else{
            goods.setFileUrl(DEFAULT_URL);
        }
    }

}
