package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.Entity;
import lombok.Data;

import java.io.Serializable;


/**
 * 会员收货地址
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-25 15:58:58
 */
@Data
@TableName("snu_customer_address")
public class CustomerAddress extends Entity<CustomerAddress> {

	private static final long serialVersionUID = 1L;

	// 主键ID
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkAddressId;

	// 会员ID
    private String fkCustomerId;

	// 名称
    private String name;

	// 手机号
    private String phone;

	// 状态
    private String status;

	// 邮编
    private String postCode;

	// 省
    private String province;

	// 市
    private String city;

	// 区/县
    private String region;

	// 详细地址
    private String detailAddress;

    private String isDefault;

    private String isSelect;

	@Override
    protected Serializable pkVal() {
		return pkAddressId;
	}



}
