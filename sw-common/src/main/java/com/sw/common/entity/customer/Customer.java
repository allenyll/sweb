package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.Entity;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author yu.leilei
 * @since 2018-10-22
 */
@Data
@TableName("snu_customer")
public class Customer extends Entity<Customer> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@TableId(type = IdType.ASSIGN_UUID)
	private String pkCustomerId;
	/**
	 * 会员名称
	 */
	private String customerName;
	/**
	 * 会员账号
	 */
	private String customerAccount;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 密码盐
	 */
	private String salt;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 微信openid
	 */
	private String openid;

	@Override
	protected Serializable pkVal() {
		return this.pkCustomerId;
	}

}
