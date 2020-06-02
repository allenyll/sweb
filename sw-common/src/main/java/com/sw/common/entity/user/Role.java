package com.sw.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.Entity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-13
 */
@Data
@TableName("sys_role")
@ToString
public class Role extends Entity<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 权限主键
     */
	@TableId(type = IdType.ASSIGN_UUID)
	private String pkRoleId;
    /**
     * 权限名称
     */
	private String roleName;
    /**
     * 权限标识
     */
	private String roleSign;
    /**
     * 备注
     */
	private String remark;

	@Override
	protected Serializable pkVal() {
		return pkRoleId;
	}
}
