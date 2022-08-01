package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Reference
    private HouseService houseService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private CommunityService communityService;
    @Reference
    private UserFollowService userFollowService;

    /**
     * 房源列表，分页及带条件查询的方法
     * RequestBody HouseQueryVo 前端发了一个post请求。这个注解将请求体转换成houseQueryVo对象,
     *
     * @return
     */
    @RequestMapping (value = "/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> findListPage(@RequestBody HouseQueryVo houseQueryVo,
                               @PathVariable Integer pageNum,
                               @PathVariable Integer pageSize) {
        //调用HouseService中前端分页及带条件查询的方法
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(pageInfo);
    }

    //跳转到房源详情页面，可以看到房源的一些信息，
    @RequestMapping("/info/{id}")
    public Result toInfoPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        //调用houseService里面查询房源的方法
        House house = houseService.getById(id);
        //获取小区信息
        Community community = communityService.getById(house.getCommunityId());
        //获取经纪人信息
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        //获取房源图片信息
        List<HouseImage> houseImage1List = houseImageService.findImageList(id, 1);
        //放到Model中
        model.addAttribute("house", house);
        model.addAttribute("community", community);
        //如果经纪人没有，页面上会显示出问题，需要做判断
        model.addAttribute("houseBrokerList", houseBrokerList);
        model.addAttribute("houseImage1List", houseImage1List);
        //设置默认没有关注房源
        //model.addAttribute("isFollow", false);
        //设置一个标记
        boolean isFollow = false;
        //从session中获取UserInfo对象
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if (userInfo != null) {
            //证明已经登录，调用userFollowService.isFollowed查看是否已经关注该房源,userInfo.getId(),id(房源id)
            Long userId = userInfo.getId();
            int count = userFollowService.isFollowed(userId, id);
            if (count > 0) {
                isFollow = true;
            }
        }
        //将isFollow放进model中,isFollow是页面的key
        model.addAttribute("isFollow", isFollow);
        return Result.ok(model);
    }
}