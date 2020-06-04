package com.sw.client.fallback;

import com.sw.client.FileFeignClient;
import com.sw.common.entity.user.File;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class FileFallbackFactory implements FallbackFactory<FileFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileFallbackFactory.class);

    @Override
    public FileFeignClient create(Throwable throwable) {
        return new FileFeignClient() {

            @Override
            public File selectOne(Map<String, Object> param) {
                LOGGER.error("获取文件失败！");
                return null;
            }

            @Override
            public void save(File file) {
                LOGGER.error("保存文件失败!");
            }

            @Override
            public List<File> list(File file) {
                LOGGER.error("获取文件列表失败!");
                return null;
            }

            @Override
            public void update(Map<String, Object> param) {
                LOGGER.error("更新文件信息失败");
            }

            @Override
            public void dealFile(Map<String, Object> param) {
                LOGGER.error("处理文件失败");
            }

            @Override
            public void deleteFile(String fkId) {
                LOGGER.error("删除文件失败");
            }

            @Override
            public void updateFile(Map<String, Object> params) {
                LOGGER.error("更新文件失败");
            }
        };
    }
}
