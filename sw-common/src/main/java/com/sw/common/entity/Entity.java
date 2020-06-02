package com.sw.common.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public abstract class Entity<T extends Entity> extends Model {

    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 创建人
     */
    private String addUser;
    /**
     * 创建时间
     */
    private String addTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 更新时间
     */
    private String updateTime;


}
