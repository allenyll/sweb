package com.sw.client.feign;

import com.sw.client.fallback.FileFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.product.GoodsParam;
import com.sw.common.entity.user.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:  feign 文件服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.FILE_SERVICE, fallbackFactory = FileFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface FileFeignClient {

    @RequestMapping(value = "file/selectOne", method = RequestMethod.POST)
    File selectOne(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "file/save", method = RequestMethod.POST)
    void save(@RequestBody File file);

    @RequestMapping(value = "file/getFiles", method = RequestMethod.POST)
    List<File> list(@RequestBody File file);

    @RequestMapping(value = "file/update", method = RequestMethod.POST)
    void update(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "file/dealFile", method = RequestMethod.POST)
    void dealFile(@RequestBody Map<String, Object> param);

    @RequestMapping(value = "file/deleteFile", method = RequestMethod.POST)
    void deleteFile(@RequestParam String fkId);

    @RequestMapping(value = "file/updateFile", method = RequestMethod.POST)
    void updateFile(@RequestBody Map<String, Object> params);
}
