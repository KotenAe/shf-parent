package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminRoleDao extends BaseDao<AdminRoleDao> {
    List<Long> findRoleIdsByAdminId(Long adminId);

    void deleteRoleIdsByAdminId(Long adminId);

    void addAdminIdAndRoleId(@Param("roleId") Long roleId, @Param("adminId") Long adminId);

}
