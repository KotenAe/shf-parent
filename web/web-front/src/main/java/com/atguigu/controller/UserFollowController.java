package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController extends BaseController {

    @Reference
    private UserFollowService userFollowService;

    /**
     * 关注房源
     *
     * @param houseId
     * @param request
     * @return
     */
    @RequestMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable Long houseId, HttpServletRequest request) {
        // 用户登录了才能关注房源，没有登录的话需要登录后重新跳转到这个页面，需要配一个拦截器，做跳转
        // 从域中获取我们之前的userInfo对象，这也是登录的前提条件，但有一个问题，session过期了，用户还在登录吗？session过期
        // request.getSession().setAttribute("USER", userInfo);
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        //拿到userInfo对象，获取userId，拿到userId后设置userFollow对象
        Long userId = userInfo.getId();
        userFollowService.follow(userId, houseId);

        return Result.ok();
    }

    /**
     * 取消关注房源
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id, HttpServletRequest request) {
        userFollowService.cancelFollow(id);
        return Result.ok();
    }

    /**
     * 我的关注
     *
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        Long userId = userInfo.getId();
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum, pageSize, userId);
        return Result.ok(pageInfo);
    }
}
