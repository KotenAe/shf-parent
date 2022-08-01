package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @CalssName:IndexController
 * @Author: Kotena
 * @Dtae: 2022/7/19/21:10
 * @Description:
 */
@Controller
public class IndexController {
    private final static String PAGE_INDEX = "frame/index";
    private final static String PAGE_MAIN = "frame/main";
    //注入提供者，使用dubbo远程调用
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
/*
    @RequestMapping
    public String toIndex() {
        return PAGE_INDEX;
    }
*/

    @RequestMapping("/main")
    public String toMain() {
        return PAGE_MAIN;
    }

    //左侧动态显示权限菜单，前台看代码，查的是admin表${admin.headUrl}和权限表 ${permissionList},重写 toIndex方法
    @RequestMapping
    public String toIndex(Model model) {
        //注入AdminService和PermissionService
        //查询admin表,通过adminId找到admin
        //先写死后续修改
        Long adminId = 1L;
        Admin admin = adminService.getById(adminId);
        //将admin放进域里面
        model.addAttribute("admin", admin);
        //查询他的权限列表，返回值是前台需要的${permissionList}
        List<Permission> permissionList = permissionService.findMenuPermissionsByAdminId(adminId);
        //放进域里面
        model.addAttribute("permissionList", permissionList);
        return PAGE_INDEX;
    }
}
