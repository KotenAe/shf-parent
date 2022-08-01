package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {
    private final static String PAGE_EDIT = "houseBroker/edit";
    private final static String PAGE_CREATE = "houseBroker/create";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String LIST_ACTION = "redirect:/house/";
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private AdminService adminService;

    /**
     * 新增经纪人
     */
    @RequestMapping("/create")
    public String create(Model model, @RequestParam Long houseId) {
        List<Admin> adminList = adminService.findAll();
        //页面上是adminList
        model.addAttribute("adminList", adminList);
        model.addAttribute("houseId", houseId);
        return PAGE_CREATE;
    }

    /**
     * 保存新增经纪人，返回新增成功页面
     *
     * @param houseBroker
     * @return
     */
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        this.houseBrokerService.insert(houseBroker);
        return PAGE_SUCCESS;
    }

    /**
     * 删除经纪人
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(Model model, @PathVariable Long houseId, @PathVariable Long id) {
        this.houseBrokerService.delete(id);
        return LIST_ACTION + houseId;
    }


    /**
     * 修改经纪人信息，返回的是集合houseBrokerList，先回显数据，在修改
     */
    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        HouseBroker houseBroker = houseBrokerService.getById(id);
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList",adminList);
        model.addAttribute("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    /**
     * 修改经纪人信息
     */
    @RequestMapping("/update")
    public String update(HouseBroker houseBroker) {
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        houseBrokerService.update(houseBroker);
        return PAGE_SUCCESS;
    }
}
