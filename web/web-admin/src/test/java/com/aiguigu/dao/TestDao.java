package com.aiguigu.dao;

import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDao {
    @Autowired
    private AdminService adminService;


}
