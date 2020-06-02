package com.sw.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.user.SysUserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 用户权限表 Mapper 接口
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-13
 */
@Repository("sysUserRoleMapper")
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
