package com.sw.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.cache.util.CacheUtil;
import com.sw.client.feign.FileFeignClient;
import com.sw.common.constants.dict.FileDict;
import com.sw.common.entity.product.*;
import com.sw.common.entity.user.User;
import com.sw.common.util.*;
import com.sw.product.mapper.GoodsMapper;
import com.sw.product.service.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品基本信息表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-21 10:51:24
 */
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    GoodsFullReduceServiceImpl goodsFullReduceService;

    @Autowired
    GoodsLadderServiceImpl goodsLadderService;

    @Autowired
    SkuServiceImpl skuService;

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    protected CacheUtil cacheUtil;

    @Autowired
    SpecsServiceImpl specsService;

    @Autowired
    SpecOptionServiceImpl specOptionService;

    @Autowired
    BrandServiceImpl brandService;

    @Autowired
    FileFeignClient fileFeignClient;

    /**
     * 创建商品
     * @param goodsParam
     * @param user
     * @return
     * @throws Exception
     */
    public int createGoods(GoodsParam goodsParam, User user) throws Exception {
        String promotionType = goodsParam.getPromotionType();
        // 无优惠不保存优惠信息
        if(!"SW2001".equals(promotionType)){
            insertRelateList(goodsFullReduceService, goodsParam.getGoodsFullReduceList(), goodsParam.getPkGoodsId());
            insertRelateList(goodsLadderService, goodsParam.getGoodsLadderList(), goodsParam.getPkGoodsId());
        }
        insertSkuStock(goodsParam.getSkuStockList(), goodsParam.getPkGoodsId(), user);
        return 1;
    }

    /**
     * 更新商品
     * @param goodsParam
     * @param user
     * @return
     */
    public int updateGoods(GoodsParam goodsParam, User user) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String promotionType = goodsParam.getPromotionType();
        // 更新商品图片
        List<Map<String, String>> fileList = goodsParam.getSelectSkuPics();
        map.put("FILE_TYPE", FileDict.GOODS.getCode());
        map.put("FK_ID", goodsParam.getPkGoodsId());
        map.put("fileList", fileList);
        fileFeignClient.updateFile(map);
        // 无优惠不保存优惠信息
        if(!"SW2001".equals(promotionType)){
            // 先删除优惠信息在新增
            deleteGoodsFullList(goodsParam);
            insertRelateList(goodsFullReduceService, goodsParam.getGoodsFullReduceList(), goodsParam.getPkGoodsId());
            deleteGoodsLadderList(goodsParam);
            insertRelateList(goodsLadderService, goodsParam.getGoodsLadderList(), goodsParam.getPkGoodsId());
        }
        deleteSkuStock(goodsParam);
        insertSkuStock(goodsParam.getSkuStockList(), goodsParam.getPkGoodsId(), user);
        return 1;
    }

    /**
     * 删除sku信息
     * @param goodsParam
     */
    private void deleteSkuStock(GoodsParam goodsParam) {
        QueryWrapper<Sku> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
        skuService.remove(entityWrapper);
    }

    /**
     * 删除满减优惠
     * @param goodsParam
     */
    private void deleteGoodsFullList(GoodsParam goodsParam) {
        QueryWrapper<GoodsFullReduce> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
        goodsFullReduceService.remove(entityWrapper);
    }

    /**
     * 删除阶梯优惠
     * @param goodsParam
     */
    private void deleteGoodsLadderList(GoodsParam goodsParam) {
        QueryWrapper<GoodsLadder> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
        goodsLadderService.remove(entityWrapper);
    }

    /**
     * 获取商品信息
     * @param goods
     * @return
     * @throws Exception
     */
    public Map<String, Object> getGoodsInfo(Goods goods) throws Exception {
        Map<String, Object> result = new HashMap<>();
        GoodsParam goodsParam = new GoodsParam();
        BeanUtil.fatherToChild(goods, goodsParam);

        // 获取父级分类
        Category category = categoryService.getById(goodsParam.getFkCategoryId());
        goodsParam.setParentCategoryId(category.getParentId());
        Category specCategory = categoryService.getById(goodsParam.getFkSpecCategoryId());
        goodsParam.setParentSpecCategoryId(specCategory.getParentId());

        String promotionType = goods.getPromotionType();
        if(!"SW2001".equals(promotionType)){
            QueryWrapper<GoodsFullReduce> wrapper = new QueryWrapper<>();
            wrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
            List<GoodsFullReduce> goodsFullReduces = goodsFullReduceService.list(wrapper);
            if(CollectionUtil.isNotEmpty(goodsFullReduces)) {
                for(GoodsFullReduce fullReduce: goodsFullReduces){
                    fullReduce.setDefault(true);
                }
            }
            goodsParam.setGoodsFullReduceList(goodsFullReduces);
            QueryWrapper<GoodsLadder> goodsLadderEntityWrapper = new QueryWrapper<>();
            goodsLadderEntityWrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
            List<GoodsLadder> goodsLadders = goodsLadderService.list(goodsLadderEntityWrapper);
            if(CollectionUtil.isNotEmpty(goodsLadders)) {
                for(GoodsLadder goodsLadder: goodsLadders){
                    goodsLadder.setDefault(true);
                }
            }
            goodsParam.setGoodsLadderList(goodsLadders);

        }

        // 获取SKU
        QueryWrapper<Sku> wrapper = new QueryWrapper<>();
        wrapper.eq("FK_GOODS_ID", goodsParam.getPkGoodsId());
        List<Sku> list = skuService.list(wrapper);
        goodsParam.setSkuStockList(list);
        List<Map<String, Object>> specValueList = new ArrayList<>();
        List<Map<String, Object>> specsList = dealSpecs(list, goodsParam, specValueList);
        LOGGER.debug("specsList: {}",specsList);
        goodsParam.setSpecsList(specsList);
        goodsParam.setSkuStockMapList(specValueList);

        // 品牌信息
        Brand brand = brandService.getById(goodsParam.getFkBrandId());
        if(brand == null){
            brand.setBrandName("品牌失效");
        }
        goodsParam.setBrand(brand);
        result.put("obj", goodsParam);

        return result;
    }

    /**
     * 处理配置的规则和sku
     * @param list
     * @param goodsParam
     * @param specValueList
     * @return
     */
    private List<Map<String, Object>> dealSpecs(List<Sku> list, GoodsParam goodsParam, List<Map<String, Object>> specValueList) {
        List<Map<String, Object>> specList = new ArrayList<>();
        List<String> specNames = new ArrayList<>();
        List<String> specOptionIds = new ArrayList<>();
        List<Map<String, Object>> specOptionList = null;
        if(CollectionUtil.isNotEmpty(list)) {
            for(Sku sku:list){
                Map<String, Object> map;
                map = sku.toMap();
                String specValue = sku.getSpecValue();
                String[] specValues = specValue.split(";");
                if(specValues.length > 1) {
                    for(int i=0; i<specValues.length;i++){
                        Set<String> values = new HashSet<>();
                        String _val = specValues[i].substring(1, specValues[i].length() - 1);
                        String[] split = _val.split(",");
                        // specOptionId
                        String specOptionId = split[0];
                        values.add(specOptionId);
                        String value = split[1];
                        map.put("value"+i, value);
                        Map<String, Object> spec = specsService.getSpecs(specOptionId);
                        if(spec == null && spec.isEmpty()) {
                            return null;
                        }
                        String name = MapUtil.getString(spec, "specName");
                        String id = name + "_" + value + "_" +specOptionId;
                        if(!specOptionIds.contains(id)){
                            specOptionList = new ArrayList<>();
                            if(CollectionUtil.isNotEmpty(specList)){
                                for(Map<String, Object> _spec:specList){
                                    String _name = MapUtil.getString(_spec, "specName");
                                    if(name.equals(_name)){
                                        specOptionList = (List<Map<String, Object>>) _spec.get("specOptionList");
                                        specOptionList.get(0).put("active", false);
                                    }
                                }
                            }
                            specOptionIds.add(id);
                            Map specOptionMap = new HashMap();
                            specOptionMap.put("id", specOptionId);
                            specOptionMap.put("name", value);
                            specOptionMap.put("active", false);
                            specOptionList.add(specOptionMap);
                            sortOption(specOptionList);
                            specOptionList.get(0).put("active", true);
                            spec.put("specOptionList", specOptionList);
                        }
                        if(!specNames.contains(name)){
                            QueryWrapper<SpecOption> entityWrapper = new QueryWrapper<>();
                            entityWrapper.eq("IS_DELETE", 0);
                            entityWrapper.eq("FK_SPECS_ID", MapUtil.getString(spec, "specId"));
                            List<SpecOption> specOptions = specOptionService.list(entityWrapper);
                            if(CollectionUtil.isNotEmpty(specOptions)){
                                spec.put("specOptions", specOptions);
                            }
                            spec.put("values", values);
                            specList.add(spec);
                            specNames.add(name);
                        }else{
                            for(Map<String, Object> _map:specList){
                                String _name = MapUtil.getString(_map, "specName");
                                if(name.equals(_name)){
                                    Set<String> set = (Set<String>) _map.get("values");
                                    set.add(specOptionId);
                                }
                            }
                        }
                    }
                }
                specValueList.add(map);
            }
        }
        return specList;
    }

    private void sortOption(List<Map<String, Object>> specOptionList) {
        if(CollectionUtil.isNotEmpty(specOptionList)){
            Collections.sort(specOptionList, (o1, o2) -> {
                String name1 = MapUtil.getString(o1, "name"); //name1是从你list里面拿出来的一个
                String name2 = MapUtil.getString(o2, "name"); //name1是从你list里面拿出来的第二个name
                if(name1.compareTo(name2) < 0){
                    return -1;
                }else if(name1.compareTo(name2) > 0){
                    return 1;
                }else{
                    return 0;
                }
            });
        }
    }

    private void insertRelateList(ServiceImpl service, List list, String id) throws Exception {
        if(CollectionUtil.isNotEmpty(list)){
            for(Object data:list){
                Method defaultMethod = data.getClass().getMethod("isDefault");
                Object isDefault = defaultMethod.invoke(data);
                if(isDefault.equals(true)) {
                    Method method = data.getClass().getMethod("setFkGoodsId", String.class);
                    method.invoke(data, id);
                    service.save(data);
                }
            }
        }
    }

    /**
     * 保存SKU
     * @param skuStockList
     * @param pkGoodsId
     */
    private void insertSkuStock(List<Sku> skuStockList, String pkGoodsId, User user) {
        String userId =  user.getPkUserId();
        if(CollectionUtil.isNotEmpty(skuStockList)){
            for(Sku sku:skuStockList) {
                dealSkuCode(sku, pkGoodsId);
                sku.setFkGoodsId(pkGoodsId);
                sku.setSkuStatus("SW1302");
                sku.setIsDelete(0);
                sku.setAddTime(DateUtil.getCurrentDateTime());
                sku.setAddUser(userId);
                sku.setUpdateTime(DateUtil.getCurrentDateTime());
                sku.setUpdateUser(userId);
                skuService.save(sku);
            }
        }
    }

    /**
     * 生成SKU编码
     * @param sku
     */
    private void dealSkuCode(Sku sku, String pkGoodsId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String no = StringUtil.getOrderNo(pkGoodsId, sdf);
        sku.setSkuCode(no);
    }

    private static void test(Object data, String id) throws Exception {
        /*Method method = object.getClass().getMethod("setFkGoodsId", String.class);
        method.invoke(object, id);*/
        Method defaultMethod = data.getClass().getMethod("isDefault");
        Object isDefaut = defaultMethod.invoke(data);
        if(isDefaut.equals(false)){
            System.out.println(isDefaut);
        }
    }


    public static void main(String[] args) throws Exception {



        /*GoodsLadder goodsLadder = new GoodsLadder();
        goodsLadder.setDefault(true);
        test(goodsLadder, "111");
        System.out.println(goodsLadder);*/
        /*String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        System.out.println(result);


        String a="c51e741b745e3da0a51a97892e06e7aa";
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(a);
        String numStr = m.replaceAll("").trim();
        if (numStr.length() > 19){
            numStr = numStr.substring(0, 18);
        }
        System.out.println(String.format("%04d", Long.parseLong(numStr)).substring(0,4));*/


       /* List<Map<String, Object>> set =  new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("sss","ssss");
        map.put("qqq","ssss");
        map.put("qq1","ssss");
        map.put("223","ssss");
        set.add(map);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("sss","ssss");
        map2.put("qqq","ssss");
        map2.put("qq1","ssss");
        map2.put("223","ssss");
        //set.add(map2);

        System.out.println(map.equals(map2));*/
      /* String s1 = "36";
       String s2 = "37";
       System.out.println(s1.compareTo(s2));*/

        List<Map<String, Object>> set =  new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("name","36");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name","37");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name","38");
        Map<String, Object> map3 = new HashMap<>();
        map3.put("name","39");
        set.add(map3);
        set.add(map1);
        set.add(map);
        set.add(map2);
        System.out.println(set);
        new GoodsServiceImpl().sortOption(set);
        System.out.println(set);
    }

}
