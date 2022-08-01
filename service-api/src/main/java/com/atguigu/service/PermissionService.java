package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionsByRoleId(Long roleId);

    void saveRolePermissionRealtionShip(Long roleId, Long[] permissionIds);

    List<Permission> findMenuPermissionsByAdminId(Long adminId);

    List<Permission> findAllMenu();
}
