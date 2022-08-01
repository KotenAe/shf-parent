package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    private final static String PAGE_INDEX = "community/index";
    private final static String PAGE_EDIT = "community/edit";
    private final static String PAGE_CREATE = "community/create";
    private final static String PAGE_DELETE = "community/delete";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String LIST_ACTION = "redirect:/community";
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;

    /**
     * 带条件的分页查询
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        //获取查询条件和分页条件acl_role_permission
        Map<String, Object> filters = super.getFilters(request);
        model.addAttribute("filters", filters);
        //根据查询条件和分页条件查询当前页的数据
        PageInfo<Community> page = this.communityService.findPage(filters);
        model.addAttribute("page", page);
        //获取beijing的区域列表
        List<Dict> areaList = this.dictService.findListByDictCode("beijing");
        model.addAttribute("areaList", areaList);
        return PAGE_INDEX;
    }

    /**
     * 新增用户
     *
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String create(Model model) {
        //获取beijing的区域列表
        List<Dict> areaList = this.dictService.findListByDictCode("beijing");
        model.addAttribute("areaList", areaList);
        return PAGE_CREATE;
    }

    @RequestMapping("/save")
    public String save(Community community) {
        this.communityService.insert(community);
        return PAGE_SUCCESS;
    }

    /**
     * 修改用户，先查询用于回显
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        //根据id查询指定的小区信息
        Community community = this.communityService.getById(id);
        model.addAttribute("community", community);
        //获取beijing的区域列表
        List<Dict> areaList = this.dictService.findListByDictCode("beijing");
        //"areaList", areaList key要和页面上的对应，后面的无所谓
        //model是为了回传数据给页面
        model.addAttribute("areaList", areaList);
        return PAGE_EDIT;
    }

    /**
     * 修改用户
     *
     * @param community
     * @return
     */
    @RequestMapping("/update")
    public String update(Community community) {
        this.communityService.update(community);
        return LIST_ACTION;
    }

    /**
     * 删除用户。根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.communityService.delete(id);
        return PAGE_DELETE;
    }
}
