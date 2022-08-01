package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {
    private final static String PAGE_EDIT = "houseUser/edit";
    private final static String PAGE_CREATE = "houseUser/create";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String LIST_ACTION = "redirect:/house/";
    @Reference
    private HouseUserService houseUserService;

    /**
     * //新增房东,对应的是hse_house_broker表
     *
     * @param model
     * @param houseId
     * @return
     */
    @RequestMapping("/create")
    public String create(Model model, @RequestParam Long houseId) {
        //注入HouseUserService，调用方法，查看页面需要什么参数，参数放在model里面还是实体类里面？放在实体类里面
        //但我们是进去了房源详情界面，去添加房东的，所以页面上会有一个houseId
        model.addAttribute("houseId", houseId);
        return PAGE_CREATE;
    }

    /**
     * 保存新增房东，返回新增成功页面
     *
     * @param houseUser
     * @return
     */
    @RequestMapping("/save")
    public String save(HouseUser houseUser) {
        //注入HouseUserService，调用方法
        houseUserService.insert(houseUser);
        return PAGE_SUCCESS;
    }

    /**
     * 删除房东
     *        opt.confirm('/houseUser/delete/[[${house.id}]]/'+id);
     * @param id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(Model model, @PathVariable Long houseId, @PathVariable Long id) {
        this.houseUserService.delete(id);
        return LIST_ACTION + houseId;
    }


    /**
     * 修改房东信息，返回的是集合houseUserList，先回显数据，在修改
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser", houseUser);
        return PAGE_EDIT;
    }

    /**
     * 保存更新
     *
     * @param houseUser
     * @return
     */
    @RequestMapping("/update")
    public String update(HouseUser houseUser) {
        houseUserService.update(houseUser);
        return PAGE_SUCCESS;
    }
}
