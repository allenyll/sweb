package com.sw.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.cache.util.CacheUtil;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.feign.FileFeignClient;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.dict.FileDict;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.product.Category;
import com.sw.common.entity.product.CategoryTree;
import com.sw.common.entity.user.File;
import com.sw.common.entity.user.User;
import com.sw.common.util.*;
import com.sw.client.controller.BaseController;
import com.sw.product.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("category")
public class CategoryController extends BaseController<CategoryServiceImpl, Category> {


    @Autowired
    FileFeignClient fileFeignClient;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    CategoryServiceImpl categoryService;

    @Override
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataResponse list() {
        DataResponse dataResponse = super.list();
        List<Category> list = (List<Category>) dataResponse.get("list");
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> newList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(Category category:list){
              /*  if("0".equals(category.getParentId())){
                    continue;
                }*/
                Map<String, String> _map = new HashMap<>();
                map.put(category.getPkCategoryId(), category.getCategoryName());
                _map.put("label", category.getCategoryName());
                _map.put("value", category.getPkCategoryId());
                _map.put("parentId", category.getParentId());
                newList.add(_map);
            }
        }
        dataResponse.put("map", map);
        dataResponse.put("list", newList);
        return dataResponse;
    }

    @ResponseBody
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public DataResponse tree(String name){
        log.info("============= {开始调用方法：tree(} =============");
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        if(StringUtil.isNotEmpty(name)){
            wrapper.like("CATEGORY_NAME", name);
        }
        List<Category> categories = service.list(wrapper);

        if(CollectionUtil.isNotEmpty(categories)){
            for(Category category:categories){
                File file = new File();
                file.setFileType(FileDict.CATEGORY.getCode());
                file.setIsDelete(0);
                file.setFkId(category.getPkCategoryId());
                List<File> sysFiles = fileFeignClient.list(file);
                if(CollectionUtil.isNotEmpty(sysFiles)){
                    category.setFileUrl(sysFiles.get(0).getFileUrl());
                }else{
                    category.setFileUrl(DEFAULT_URL);
                }
            }
        }

        List<CategoryTree> list = getCategoryTree(categories, BaseConstants.MENU_ROOT);

        result.put("list", list);
        log.info("============= {结束调用方法：tree(} =============");
        return DataResponse.success(result);
    }

    @ResponseBody
    @RequestMapping(value = "categoryTree", method = RequestMethod.GET)
    public DataResponse categoryTree(){
        DataResponse dataResponse = super.list();
        List<Category> list = (List<Category>) dataResponse.get("list");
        if(!CollectionUtils.isEmpty(list)){
            for(Category _category:list){
                setParentCategory(_category);
            }
        }

        Category category = new Category();
        category.setPkCategoryId("0");
        category.setIsDelete(0);
        category.setCategoryName("顶级节点");
        category.setCategoryNo("top");
        category.setParentId("top");
        list.add(category);

        List<CategoryTree> trees = getCategoryTree(list, "top");

        dataResponse.put("categoryTree", trees);
        return dataResponse;
    }

    @ResponseBody
    @RequestMapping(value = "getCategoryInfo/{id}", method = RequestMethod.GET)
    public DataResponse getCategoryInfo(@PathVariable String id){

        DataResponse dataResponse = super.get(id);
        Category category = (Category) dataResponse.get("obj");

        // 获取同级分类
        if(category == null){
            return DataResponse.fail("没有获取到对应的分类");
        }

        // 获取子分类
        QueryWrapper<Category> childWrapper = new QueryWrapper<>();
        childWrapper.eq("PARENT_ID", category.getParentId());
        childWrapper.eq("IS_USED", "SW1302");
        childWrapper.eq("IS_DELETE", 0);
        childWrapper.orderBy(true, false, "CATEGORY_NO");

        List<Category> brotherCategoryList = categoryService.list(childWrapper);

        List<CategoryTree> trees = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(brotherCategoryList)){
            for (Category _category:brotherCategoryList){
                CategoryTree categoryTree = setCategoryTree(_category);
                trees.add(categoryTree);
            }
        }

        dataResponse.put("list", trees);
        dataResponse.put("obj", setCategoryTree(category));

        log.info("==================结束调用 get================");

        return dataResponse;
    }

    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DataResponse get(@PathVariable String id){

        DataResponse dataResponse = super.get(id);
        Category category = (Category) dataResponse.get("obj");

        if(BaseConstants.MENU_ROOT.equals(id)){
            category = new Category();
            category.setPkCategoryId(id);
            category.setCategoryName("顶级节点");
        }else{
            setParentCategory(category);
        }

        File file = getFile(category);
        if(file != null){
            category.setFileUrl(file.getFileUrl());
        }else{
            file = new File();
        }

        // 获取子分类
        QueryWrapper<Category> childWrapper = new QueryWrapper<>();
        childWrapper.eq("PARENT_ID", id);
        childWrapper.eq("IS_DELETE", 0);
        childWrapper.eq("IS_USED", "SW1302");
        childWrapper.orderBy(true, false, "CATEGORY_NO");
        List<Category> childCategoryList = categoryService.list(childWrapper);
        childCategoryList.add(category);
        for(Category child:childCategoryList){
            File _file = getFile(child);
            if(_file != null){
                child.setFileUrl(_file.getFileUrl());
            } else {
                child.setFileUrl(DEFAULT_URL);
            }
        }

        if(!"0".equals(id)){
            List<CategoryTree> trees = getCategoryTree(childCategoryList, "0");
            dataResponse.put("tree", trees);
        }

        dataResponse.put("file", file);
        dataResponse.put("obj", category);

        log.info("==================结束调用 get================");

        return dataResponse;
    }

    private File getFile(Category category) {
        File file = new File();
        file.setFileType(FileDict.CATEGORY.getCode());
        file.setIsDelete(0);
        file.setFkId(category.getPkCategoryId());
        List<File> _sysFiles = fileFeignClient.list(file);
        File _file = null;
        if(CollectionUtil.isNotEmpty(_sysFiles)){
            _file = _sysFiles.get(0);
        }
        return _file;
    }

    private CategoryTree setCategoryTree(Category category){
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setId(category.getPkCategoryId());
        categoryTree.setParentId(category.getParentId());
        categoryTree.setName(category.getCategoryName());
        categoryTree.setCode(category.getCategoryNo());
        categoryTree.setLabel(category.getCategoryName());
        categoryTree.setTitle(category.getCategoryName());
        categoryTree.setLevel(category.getCategoryLevel());
        categoryTree.setIsUsed(StatusDict.codeToMessage(category.getIsUsed()));
        return categoryTree;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DataResponse add(@CurrentUser(isFull = true) User user, @RequestBody Category category) {
        String id = StringUtil.getUUID32();
        category.setPkCategoryId(id);
        super.add(user, category);
        List<Map<String, String>> fileList = category.getFileList();
        if(CollectionUtil.isNotEmpty(fileList)){
            Map<String, String> map = fileList.get(0);
            String url = MapUtil.getMapValue(map, "url");
            String userId = cacheUtil.get("userId");
            // 存入数据库
            File sysFile = new File();
            sysFile.setFileType(FileDict.CATEGORY.getCode());
            sysFile.setFkId(id);
            sysFile.setFileUrl(url);
            sysFile.setAddTime(DateUtil.getCurrentDateTime());
            sysFile.setIsDelete(0);
            sysFile.setAddUser(userId);
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            fileFeignClient.save(sysFile);
        }
        return DataResponse.success();
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "{id}",method = RequestMethod.PUT)
    public DataResponse update(@CurrentUser(isFull = true) User user,@RequestBody Category category) {
        String userId = cacheUtil.get("userId");
        List<Map<String, String>> fileList = category.getFileList();
        if(CollectionUtil.isNotEmpty(fileList)){
            Map<String, String> map = fileList.get(0);
            String url = MapUtil.getMapValue(map, "url");
            Map<String, Object> _map = new HashMap<>();
            _map.put("FILE_TYPE", FileDict.CATEGORY.getCode());
            _map.put("FK_ID", category.getPkCategoryId());
            _map.put("URL", url);
            _map.put("USER_ID", userId);
            fileFeignClient.dealFile(_map);
            log.info("处理文件成功");
        } else {
            // 删除对应的图片
            fileFeignClient.deleteFile(category.getPkCategoryId());
        }
        return super.update(user, category);
    }

    private void setParentCategory(Category category) {
        String parentId = category.getParentId();
        if(parentId.equals(BaseConstants.MENU_ROOT)){
            category.setParentCategoryName("顶级节点");
        }else{
            QueryWrapper<Category> entityWrapper = new QueryWrapper<>();
            entityWrapper.eq("IS_DELETE", 0);
            entityWrapper.eq("PK_CATEGORY_ID", parentId);
            Category _category = service.getOne(entityWrapper);
            if(_category != null){
                category.setParentCategoryName(_category.getCategoryName());
            }
        }
    }

    private List<CategoryTree> getCategoryTree(List<Category> list, String rootId) {
        List<CategoryTree> trees = new ArrayList<>();
        CategoryTree tree;
        if(CollectionUtil.isNotEmpty(list)){
            for(Category obj:list){
                tree = new CategoryTree();
                tree.setId(obj.getPkCategoryId());
                tree.setParentId(obj.getParentId());
                tree.setName(obj.getCategoryName());
                tree.setCode(obj.getCategoryNo());
                tree.setTitle(obj.getCategoryName());
                tree.setLabel(obj.getCategoryName());
                tree.setLevel(obj.getCategoryLevel());
                tree.setIsUsed(StatusDict.codeToMessage(obj.getIsUsed()));
                tree.setUrl(obj.getFileUrl());
                trees.add(tree);
            }
        }
        return TreeUtil.build(trees, rootId);
    }

}
