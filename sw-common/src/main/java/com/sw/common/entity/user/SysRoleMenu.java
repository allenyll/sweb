package com.sw.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 权限菜单关系
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-13
 */
@Data
@ToString
@TableName("sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 关系主键
     */
	@TableId(type = IdType.ASSIGN_UUID)
	private String pkRelationId;
    /**
     * 权限ID
     */
	private String fkRoleId;
    /**
     * 菜单ID
     */
	private String fkMenuId;


	@Override
	protected Serializable pkVal() {
		return this.pkRelationId;
	}

}
