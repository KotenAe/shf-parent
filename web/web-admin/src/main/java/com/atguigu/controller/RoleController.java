package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @CalssName:RoleController
 * @Author: Kotena
 * @Dtae: 2022/7/19/20:50
 * @Description:
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final static String PAGE_CREATE = "role/create";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String PAGE_EDIT = "role/edit";
    private final static String LIST_ACTION = "redirect:/role";
    private final static String PAGE_INDEX = "role/index";
    private final static String PAGE_ASSIGN_SHOW = "role/assignShow";
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    //新增按钮跳转的界面
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 新增用户后对Dao操作的处理器,并设置添加成功够返回的页面
     *
     * @param role
     * @param request
     * @return
     */
    @RequestMapping("/save")
    public String save(Role role, HttpServletRequest request) {
        this.roleService.insert(role);
        return PAGE_SUCCESS;
    }

    //根据id查询，从前面表单传来的参数,使用restful风格，用于数据回显
    @RequestMapping("/edit/{id}")
    //存在里面
    public String edit(Model model, @PathVariable Long id) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        //返回edit页面，
        return PAGE_EDIT;

    }

    //修改用户
    @RequestMapping("/update")

    public String update(Role role) {
        roleService.update(role);
        //返回edit页面，
        return PAGE_SUCCESS;

    }

    //删除数据
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_ACTION;
    }

    //分页支持
    @RequestMapping
    public String findPage(Model model, HttpServletRequest request) {
        //获取查询条件(roleName)和分页条件(pageNum,pageSize)
        Map<String, Object> filters = getFilters(request);
        //访问业务层完成分页查询
        PageInfo<Role> page = this.roleService.findPage(filters);
        model.addAttribute("page", page);//分页数据：当前页数据，分页条的参数
        model.addAttribute("filters", filters);//查询条件和分页的两个参数（pageNum，pageSize）
        return PAGE_INDEX;
    }

    /**
     * 去分配权限的页面，观察页面var zNodes = [[${zNodes}]];要从request域里面得到一个zNdes,还需要将roleId放到request域里面
     * 放到Request域里面，我们用一个Model就可以了
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/assignShow/{roleId}")
    public String goAssignShow(@PathVariable Long roleId, Model model) {
        //将roleId放到request域里面
        model.addAttribute("roleId", roleId);
        //给角色分配权限，调用根据角色id获取权限的方法，返回是一个List里面是一个Map
        List<Map<String, Object>> zNodes = permissionService.findPermissionsByRoleId(roleId);
        //将zNodes放到request域里面
        model.addAttribute("zNodes", zNodes);
        return PAGE_ASSIGN_SHOW;
    }

    //保存权限分配
    @RequestMapping("/assignPermission")
    public String assignPermission(Long roleId, Long[] permissionIds) {
        //给角色分配权限，(roleId, permissionIds);
        permissionService.saveRolePermissionRealtionShip(roleId, permissionIds);
        return PAGE_SUCCESS;
    }
}
