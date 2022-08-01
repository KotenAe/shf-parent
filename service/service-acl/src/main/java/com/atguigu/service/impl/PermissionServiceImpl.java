package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    protected BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    /**
     * 根据RoleId查询权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> findPermissionsByRoleId(Long roleId) {
        //注入PermissionDao，调用查询方法，查询所有返回一个集合
        List<Permission> permissionList = permissionDao.findAll();
        //根据角色id判断已分配权限的Id
        List<Long> permissionRoleIds = rolePermissionDao.findPermissionsByRoleId(roleId);
        //创建返回的List, 构建zNodes  { id:221, pId:22, name:"随意勾选 2-2-1", checked:true},
        List<Map<String, Object>> returnList = new ArrayList<>();
        for (Permission permission : permissionList) {
            Map<String, Object> map = new HashMap<>();

            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            //判断当前权限的id在不在permissionRoleIds里面
            if (permissionRoleIds.contains(permission.getId())) {
                //证明该权限已经分配
                map.put("checked", true);
            }
            returnList.add(map);
        }
        return returnList;
    }

    /**
     * 保存权限
     * @param roleId
     * @param permissionIds
     */
    @Override
    public void saveRolePermissionRealtionShip(Long roleId, Long[] permissionIds) {
        rolePermissionDao.deleteByRoleId(roleId);
        //第二种方法，直接封装成对象，用通用的增删改查
        for (Long permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId)) continue;
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionDao.insert(rolePermission);
        }
    }

    @Override
    public List<Permission> findMenuPermissionsByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        //admin账号id为：1
        if (adminId == 1) {
            //如果是超级管理员，获取所有菜单
            permissionList = permissionDao.findAll();
        } else {
            permissionList = permissionDao.findPermissionsListByAdminId(adminId);
        }
        //把权限数据构建成树形结构数据，这个类放在哪里？放在Modell里面
        List<Permission> zTree = PermissionHelper.bulid(permissionList);
        return zTree;
    }

    /**
     * 查找全部菜单
     * @return
     */
    @Override
    public List<Permission> findAllMenu() {
        //全部权限列表
        List<Permission> permissionList = permissionDao.findAll();
        if(CollectionUtils.isEmpty(permissionList)) return null;
        //构建树形数据,总共三级
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }
}