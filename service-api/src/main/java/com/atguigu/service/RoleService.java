package com.atguigu.service;


import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName:RoleService
 * @Author: Kotena
 * @Dtae: 2022/7/19/19:53
 * @Description: TODD
 */
public interface RoleService extends BaseService<Role> {
    List<Role> findAll();
    //用户分配角色的功能，查询所有角色
    Map<String, Object> findRoleByAdminId(Long adminId);
    //分配角色功能，保存分配角色
    void assignRole(Long adminId, Long[] roleIds);
}
