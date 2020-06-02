package com.sw.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.user.SysRoleMenu;
import com.sw.user.mapper.SysRoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限菜单关系 服务实现类
 * </p>
 *
 * @author yu.leilei
 * @since 2018-11-13
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {

}
