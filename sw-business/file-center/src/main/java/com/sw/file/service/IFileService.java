package com.sw.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.user.File;
import com.sw.common.entity.user.User;

public interface IFileService extends IService<File> {

    /**
     * 根据fkId删除文件
     * @param fkId
     */
    void deleteFile(String fkId);
}
