package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;

import java.util.List;

public interface RolePermissionDao extends BaseDao<RolePermission> {
    //根据角色查权限
    List<Long> findPermissionsByRoleId(Long roleId);
    //删除
    void deleteByRoleId(Long roleId);
}
