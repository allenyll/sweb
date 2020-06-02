package com.sw.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.Entity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 记录日志
 * </p>
 *
 * @author yu.leilei
 * @since 2018-12-23
 */
@TableName("sys_log")
@Data
@ToString
public class Log extends Entity<Log> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
	private String pkLogId;
    /**
     * 操作人ID
     */
	private String fkUserId;
    /**
     * 操作人账号
     */
	private String account;
    /**
     * 日志类型
     */
	private String logType;

    /**
     * 操作事件
     */
	private String operation;
    /**
     * 操作时长
     */
	private Long operateTime;
    /**
     * 操作类
     */
	private String className;
    /**
     * 参数
     */
	private String params;
    /**
     * ip
     */
	private String ip;

	@Override
	protected Serializable pkVal() {
		return this.pkLogId;
	}

}
