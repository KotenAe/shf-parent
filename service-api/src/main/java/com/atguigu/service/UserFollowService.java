package com.atguigu.service;


import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    /**
     * 关注房源
     *
     * @param userId
     * @param houseId
     */
    void follow(Long userId, Long houseId);

    /**
     * 用户是否关注房源
     *
     * @param userId
     * @param houseId
     * @return
     */
    int isFollowed(Long userId, Long houseId);

    /**
     * 取消关注房源
     *
     * @param id
     */
    void cancelFollow(Long id);

    /**
     * 我的关注
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId);
}
