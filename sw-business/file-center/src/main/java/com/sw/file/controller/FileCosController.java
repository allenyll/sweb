package com.sw.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.common.entity.user.File;
import com.sw.common.entity.user.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.client.controller.BaseController;
import com.sw.file.service.IFileService;
import com.sw.file.service.impl.FileServiceImpl;
import com.sw.file.util.CosFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:  腾讯云COS文件上传下载
 * @Author:       allenyll
 * @Date:         2019-05-24 17:38
 * @Version:      1.0
 */
@Slf4j
@RestController
@Api(value = "腾讯云文件上传接口")
@RequestMapping("/file")
public class FileCosController extends BaseController<FileServiceImpl, File> {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String URL = "https://system-web-1257935390.cos.ap-chengdu.myqcloud.com";

    @Autowired
    IFileService fileService;

    @ApiOperation("上传文件")
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public DataResponse upload(@RequestParam("file") MultipartFile file, @RequestParam String type, @RequestParam String id) throws IOException {
        if(file == null) {
            return DataResponse.fail("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        String preFix = fileName.substring(fileName.lastIndexOf("."));
        Date date = new Date();
        String time = sdf.format(date);
       // 用uuid作为文件名，防止生成的临时文件重复
       final java.io.File excelFile = java.io.File.createTempFile("imagesFile-"+time, preFix);
       /* 将MultipartFile转为File */
       file.transferTo(excelFile);

       Map<String, Object> map = CosFileUtil.uploadFile(excelFile);

       if(map == null && map.isEmpty()){
           return DataResponse.fail("上传失败");
       }

       String url = MapUtil.getString(map, "fileName", "");

       String downloadUrl = MapUtil.getString(map, "url", "");

       if(!"SW1803".equals(type)){
           // 存入数据库
           File sysFile = new File();
           sysFile.setFileType(type);
           sysFile.setFkId(id);
           sysFile.setFileUrl(URL + url);
           sysFile.setIsDelete(0);
           sysFile.setAddTime(DateUtil.getCurrentDateTime());
           sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
           fileService.save(sysFile);
       }

       Map<String,Object> result = new HashMap<>();
       result.put("url", URL + url);

       return DataResponse.success(result);
    }

    @RequestMapping(value = "/getFileList", method = RequestMethod.POST)
    public DataResponse getList(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FILE_TYPE", MapUtil.getMapValue(params, "type"));
        wrapper.eq("FK_ID",MapUtil.getMapValue(params, "id"));
        List<File> list = service.list(wrapper);
        List<Map<String, String>> newList  = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for (File file:list){
                Map<String, String> map = new HashMap<>();
                map.put("url", file.getFileUrl());
                map.put("id", file.getPkFileId());
                newList.add(map);
            }
        }

        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation(value = "获取附件文件")
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public File selectOne(@RequestBody Map<String, Object> param) {
        QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
        fileEntityWrapper.eq("FILE_TYPE", MapUtil.getString(param, "FILE_TYPE"));
        fileEntityWrapper.eq("IS_DELETE", 0);
        fileEntityWrapper.eq("FK_ID", MapUtil.getString(param, "FK_ID"));
        return service.getOne(fileEntityWrapper);
    }

    @ApiOperation("保存文件")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@CurrentUser(isFull = true) User user, @RequestBody File file) {
        super.add(user, file);
    }

    @ResponseBody
    @ApiOperation("获取文件列表")
    @RequestMapping(value = "/getFiles", method = RequestMethod.POST)
    public List<File> getFileList(@RequestBody File file) {
        QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
        fileEntityWrapper.eq("FILE_TYPE", file.getFileType());
        fileEntityWrapper.eq("IS_DELETE", 0);
        fileEntityWrapper.eq("FK_ID", file.getFkId());
        return service.list(fileEntityWrapper);
    }

    @ApiOperation("更新文件")
    @RequestMapping(value = "/dealFile", method = RequestMethod.POST)
    public void dealFile(@RequestBody Map<String, Object> param) {
        String userId = MapUtil.getString(param, "USER_ID");
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("FILE_TYPE", MapUtil.getString(param, "FILE_TYPE"));
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FK_ID", MapUtil.getString(param, "FK_ID"));
        File sysFile = service.getOne(wrapper);
        // 存入数据库
        if(sysFile != null){
            sysFile.setFileUrl(MapUtil.getString(param, "URL"));
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            service.update(sysFile, wrapper);
        }else{
            sysFile = new File();
            sysFile.setFileType("SW1802");
            sysFile.setFkId(MapUtil.getString(param, "FK_ID"));
            sysFile.setFileUrl(MapUtil.getString(param, "URL"));
            sysFile.setAddTime(DateUtil.getCurrentDateTime());
            sysFile.setIsDelete(0);
            sysFile.setAddUser(userId);
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            service.save(sysFile);
        }
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public void deleteFile(@RequestParam String fkId) {
        fileService.deleteFile(fkId);
    }

}
