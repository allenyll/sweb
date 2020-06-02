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
 * 菜单管理
 * </p>
 *
 * @author yu.leilei
 * @since 2018-06-12
 */
@TableName("sys_menu")
@Data
@ToString
public class Menu extends Entity<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
	private String pkMenuId;
    /**
     * 菜单名称
     */
	private String parentMenuId;

	@TableField(exist = false)
	private String parentMenuName;
    /**
     * 菜单名称
     */
	private String menuName;
    /**
     * 菜单跳转地址
     */
	private String menuUrl;
    /**
     * 菜单权限
     */
	private String menuPerms;
    /**
     * 菜单类型
     */
	private String menuType;
	/**
	 * 菜单编码
	 */
	private String menuCode;
    /**
     * 菜单图标
     */
	private String menuIcon;
    /**
     * 排序
     */
	private Integer sortNum;

	@Override
	protected Serializable pkVal() {
		return this.pkMenuId;
	}
}
