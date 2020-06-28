package com.sw.common.entity.cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.Entity;
import lombok.Data;

import java.io.Serializable;


/**
 *
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-27 14:46:25
 */
@Data
@TableName("snu_search_history")
public class SearchHistory extends Entity<SearchHistory> {

	private static final long serialVersionUID = 1L;

	// 主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkSearchHistoryId;

	// 关键字
    private String keyword;

	// 搜索来源，如PC、小程序、APP等
    private String from;

	// 会员Id
    private String userId;

	@Override
    protected Serializable pkVal() {
		return pkSearchHistoryId;
	}



}
