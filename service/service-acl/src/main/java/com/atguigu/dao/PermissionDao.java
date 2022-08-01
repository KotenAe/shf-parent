package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionDao extends BaseDao<Permission> {

    //查询所有权限
    List<Permission> findAll();
    //左侧动态菜单
    List<Permission> findPermissionsListByAdminId(Long adminId);
}
