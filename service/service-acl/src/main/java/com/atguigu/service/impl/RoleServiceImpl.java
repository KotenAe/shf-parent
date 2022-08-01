package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    protected BaseDao<Role> getEntityDao() {
        return roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return this.roleDao.findAll();
    }


    @Autowired
    private AdminRoleDao adminRoleDao;

    /**
     * 根据adminID查询，并做逻辑判断
     *
     * @param adminId
     * @return
     */
    @Override
    public Map<String, Object> findRoleByAdminId(Long adminId) {
        //注入RoleDao，查询所有角色，返回的是封装的Role对象的List
        List<Role> allRoleList = roleDao.findAll();
        //给角色分类，已经分配的，和没有分配的
        //先根据AdminId查询他所有的角色，查的是中间表，acl_admin_role，通过adminId，查询出来的放在集合里面，在在后面做区分
        List<Long> roleIds = adminRoleDao.findRoleIdsByAdminId(adminId);
        //创建两个List对角色进行区分
        List<Role> noAssignRoleList = new ArrayList<>();
        List<Role> assignRoleList = new ArrayList<>();
        //遍历allRoleList放到List里面
        for (Role role : allRoleList) {
            //做判断，看是否已经分配角色
            if (roleIds.contains(role.getId())) {
                assignRoleList.add(role);
            } else {
                noAssignRoleList.add(role);
            }
        }
        //创建一个集合，把两个分类作为元素放进对象模型中，供前端页面访问key是引号引起来的，和页面上一样的，value是已经分类的list
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("noAssignRoleList", noAssignRoleList);
        roleMap.put("assignRoleList", assignRoleList);
        return roleMap;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //根据用户id将已经分配的ID删除
        //遍历所有的角色id
        adminRoleDao.deleteRoleIdsByAdminId(adminId);
        for (Long roleId : roleIds) {
            /*还可以封装对象，直接利用基础的增删改查操作插入
             *    for(Long roleId : roleIds) {
             *       if(StringUtils.isEmpty(roleId)) continue;
             *       AdminRole userRole = new AdminRole();
             *       userRole.setAdminId(adminId);
             *       userRole.setRoleId(roleId);
             *       adminRoleDao.insert(userRole);
             */
            if (roleId != null) {
                adminRoleDao.addAdminIdAndRoleId(roleId, adminId);
            }
        }
    }

}
