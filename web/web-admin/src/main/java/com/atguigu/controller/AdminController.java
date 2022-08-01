package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    //跳转用户管理界面
    private final static String PAGE_INDEX = "admin/index";
    private final static String PAGE_EDIT = "admin/edit";
    private final static String PAGE_CREATE = "admin/create";
    private final static String PAGE_DELETE = "admin/delete";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String LIST_ACTION = "redirect:/admin";
    private final static String PAGE_UPLOAD = "admin/upload";
    private final static String PAGE_ASSIGNSHOW = "admin/assignShow";
    @Reference
    private AdminService adminService;

    /**
     * 跳转页面，带条件的分页查询
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping
    public String findPage(HttpServletRequest request, Model model) {
        Map<String, Object> filters = getFilters(request);
        PageInfo<Admin> page = this.adminService.findPage(filters);
        //分页数据，当前页数据，分页的条数
        model.addAttribute("page", page);
        //查询条件和分页的两个参数pageNum，pageSize
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    //新增页面
    @RequestMapping("/create")
    public String create() {

        return PAGE_CREATE;
    }

    /**
     * 新增用户后对Dao操作的处理器,并设置添加成功够返回的页面，并设置新增用户的头像
     *
     * @param admin
     * @param request
     * @return
     */
    @RequestMapping("/save")
    public String save(Admin admin, HttpServletRequest request) {
        admin.setHeadUrl("http://m.imeitou.com/uploads/allimg/220721/7-220H1104H4.jpg");
        this.adminService.insert(admin);
        return PAGE_SUCCESS;
    }

    /**
     * 删除用户，根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_ACTION;
    }

    /**
     * 数据回显，先查再修改
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin", admin);
        return PAGE_EDIT;
    }

    /**
     * 修改用户
     *
     * @param admin
     * @return
     */
    @RequestMapping("/update")

    public String update(Admin admin) {
        adminService.update(admin);
        //返回edit页面，
        return PAGE_SUCCESS;

    }

    /**
     * 去上传头像页面
     *
     * @param id
     * @param model
     * @return
     */

    @RequestMapping("/uploadShow/{id}")
    public String toUploadPage(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return PAGE_UPLOAD;
    }

    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable Long id, MultipartFile file) {
        //调用AdminService里面的getById，获取对象
        try {
            Admin admin = adminService.getById(id);
            //获取字节数组
            byte[] bytes = file.getBytes();
            //通过UUID随机生成文件名
            String fileName = UUID.randomUUID().toString();
            //通过七牛工具类将文件上传到
            QiniuUtil.upload2Qiniu(bytes, fileName);
            //给用户设置头像
            admin.setHeadUrl("https://photo.sneoo.com/" + fileName);
            //调用adminService中更新的方法
            adminService.update(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PAGE_SUCCESS;
    }

    /**
     * 去分配角色的页面,页面上携带参数，adminID,需要回显数据，包括已经分配的角色，还有没有分配的角色
     * 两个，noAssignRoleList，assignRoleList，
     * 整个的操作分为两个步骤，
     * 一、显示数据，查询所有角色，将其分成两部分，接口实现类里面做判断，一部分已经分配了的，另一部分还没有分配的
     * 二、分配角色。
     *
     * @param adminId
     * @return
     */
    @Reference
    private RoleService roleService;

    @RequestMapping("/assignShow/{adminId}")
    public String toAssignShowPage(@PathVariable Long adminId, Model model) {
        //查询所有角色，注入RoleService，放在Map中
        Map<String, Object> rolesMap = roleService.findRoleByAdminId(adminId);
        //为什么要把数据继续放在域中？从业务逻辑层返回的东西直接放进去，共享到域中，方便页面访问
        model.addAllAttributes(rolesMap);
        model.addAttribute("adminId", adminId);
        return PAGE_ASSIGNSHOW;
    }

    //分配角色，分配角色无非就是给用户设置角色
    //操作的表是中间表，我们现在可以拿到的参数，adminId，添加角色就是先把之前设置的删掉，然后重新设置，form表单提交的地址是assignRole
    @RequestMapping("/assignRole")
    public String save(Long adminId, Long[] roleIds) {
        //如果直接调用AdminRoleDao那么逻辑判断就不好做，所以调用RoleService里面分配角色的方法
        roleService.assignRole(adminId, roleIds);
        return PAGE_SUCCESS;
    }
}
