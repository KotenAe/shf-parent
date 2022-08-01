package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private final static String PAGE_INDEX = "house/index";
    private final static String PAGE_SHOW = "house/show";
    private final static String PAGE_EDIT = "house/edit";
    private final static String PAGE_CREATE = "house/create";
    private final static String PAGE_SUCCESS = "common/successPage";
    private final static String LIST_ACTION = "redirect:/house";
    @Reference
    private HouseService houseService;
    //调用CommunityService里面的方法查询所有？？？？？
    @Reference
    private CommunityService communityService;
    //需要调用DictService接口中的findListByDictCode根据这个进行下拉列表的分类显示
    @Reference
    private DictService dictService;
    @Reference
    private HouseUserService houseUserService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;

    /**
     * 页面跳转。带条件的分页查询
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        //getFilters是封装好了的，直接拿来用
        Map<String, Object> filters = getFilters(request);
        //根据查条件和分页条件查询当前页数据
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("filters", filters);
        //page这个数据包括了当前页的数据，分页条的数据，filters里面有查询条件和(pageNum,pageSize)
        model.addAttribute("page", page);
        //选择小区
        model.addAttribute("communityList", communityService.findAll());
        //户型
        model.addAttribute("houseTypeList", dictService.findListByDictCode("houseType"));
        //楼层
        model.addAttribute("floorList", dictService.findListByDictCode("floor"));
        //建筑结构
        model.addAttribute("buildStructureList", dictService.findListByDictCode("buildStructure"));
        //朝向
        model.addAttribute("directionList", dictService.findListByDictCode("direction"));
        //装修情况
        model.addAttribute("decorationList", dictService.findListByDictCode("decoration"));
        //房屋用途
        model.addAttribute("houseUseList", dictService.findListByDictCode("houseUse"));
        return PAGE_INDEX;
    }

    /**
     * 新增房源，里面的其他下拉选项
     *
     * @param model
     * @return
     */
    @RequestMapping("/create")
    public String cerate(Model model) {
        //这一句不太理解？？？？？？？？？？
        model.addAttribute("communityList", communityService.findAll());
        //户型：
        model.addAttribute("houseTypeList", dictService.findListByDictCode("houseType"));
        //楼层：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("floorList", dictService.findListByDictCode("floor"));
        //建筑结构：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("buildStructureList", dictService.findListByDictCode("buildStructure"));
        //朝向：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("directionList", dictService.findListByDictCode("direction"));
        //装修情况：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("decorationList", dictService.findListByDictCode("decoration"));
        //房屋用途：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("houseUseList", dictService.findListByDictCode("houseUse"));
        return PAGE_CREATE;
    }

    /**
     * 新增房源，保存新增
     * 往hse_house表表里面添加数据
     * 调用houseService里面的方法inster，由于是基础的增删改查，已经做了公共的抽取和继承，所以只需在HouseDao里面写SQL就行了
     *
     * @param house
     * @return
     */
    @RequestMapping("/save")
    public String save(House house) {
        this.houseService.insert(house);
        return PAGE_SUCCESS;
    }

    /**
     * 删除房源，根据id删除，HouseDao.xml里面写删除语句
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.houseService.delete(id);
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
        House house = this.houseService.getById(id);
        model.addAttribute("house", house);
        model.addAttribute("communityList", communityService.findAll());
        //户型：
        model.addAttribute("houseTypeList", dictService.findListByDictCode("houseType"));
        //楼层：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("floorList", dictService.findListByDictCode("floor"));
        //建筑结构：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("buildStructureList", dictService.findListByDictCode("buildStructure"));
        //朝向：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("directionList", dictService.findListByDictCode("direction"));
        //装修情况：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("decorationList", dictService.findListByDictCode("decoration"));
        //房屋用途：是字典表里面的数据，添加调用dictService里面的方法
        model.addAttribute("houseUseList", dictService.findListByDictCode("houseUse"));
        return PAGE_EDIT;
    }

    /**
     * 修改房源,保存新增，返回修改成功界面，HouseDao.xml里面写修改语句
     *
     * @param house
     * @return
     */
    @RequestMapping("/update")

    public String update(House house) {
        this.houseService.update(house);
        return PAGE_SUCCESS;

    }

    /**
     * 房源详情页面，根据id进入详情页面,里面需要展示的数据很多
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    public String detail(Model model, @PathVariable Long id) {
        //通过id获取表中的一行数据，是一个对象
        House house = houseService.getById(id);
        //可以找到areald=110234,和plateid=110243,而且这两个值，对应着字典表里面的id，我们可以通过重写，getId方法，用拿到的值赋给区域的名字和板块，但是没有区域名字，
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("house", house);
        model.addAttribute("community", community);
        //展示经纪人信息
        model.addAttribute("houseBrokerList", houseBrokerService.findListByHouseId(id));
        //展示房东信息，注入房东HouseUserService，需要返回值接收
        model.addAttribute("houseUserList", houseUserService.findListByHouseId(id));
        //1：普通图片 2：房产图片，都是从数据库里面拿到的，我们得通过houseId去找
        List<HouseImage> houseImage1List = houseImageService.findImageList(id, 1);
        List<HouseImage> houseImage2List = houseImageService.findImageList(id, 2);
        model.addAttribute("houseImage1List", houseImage1List);
        model.addAttribute("houseImage2List", houseImage2List);
        return PAGE_SHOW;
    }

    /**
     * 房源管理，修改房源状态信息，0代表未发布，1代表已经发布
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/publish/{id}/{status}")
    public String status(@PathVariable Long id, @PathVariable Integer status) {
        //页面上已经做了判断了，直接调用dao里面的方法,传入页面拿到的参数，返回值类型是什么？void，
        houseService.publish(id, status);
        //重定向到房源管理页面
        return LIST_ACTION;
    }
}