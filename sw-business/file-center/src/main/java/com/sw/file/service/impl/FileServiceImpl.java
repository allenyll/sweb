package com.sw.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.user.File;
import com.sw.common.entity.user.User;
import com.sw.common.util.DateUtil;
import com.sw.file.mapper.FileMapper;
import com.sw.file.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件相关信息
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-26 21:28:23
 */
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileMapper fileMapper;

    @Override
    public void deleteFile(String fkId) {
        Map<String, String> params = new HashMap<>();
        params.put("TIME", DateUtil.getCurrentDateTime());
        params.put("FK_ID", fkId);
        fileMapper.deleteFile(params);
    }
}
