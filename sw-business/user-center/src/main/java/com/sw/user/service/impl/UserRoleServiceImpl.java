package com.sw.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.user.SysUserRole;
import com.sw.user.mapper.UserRoleMapper;
import com.sw.user.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Description:  用户<user>服务实现
 * @Author:       allenyll
 * @Date:         2020/5/4 8:50 下午
 * @Version:      1.0
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRole> implements IUserRoleService {
}
