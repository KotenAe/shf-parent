package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserFollowService.class)

public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    @Autowired
    private UserFollowDao userFollowDao;

    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    /**
     * 关注房源
     *
     * @param userId
     * @param houseId
     */
    @Transactional
    @Override
    public void follow(Long userId, Long houseId) {
        //对参数进行进一步封装，因为inster(T t)是实体
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(houseId);
        userFollowDao.insert(userFollow);
    }

    /**
     * 是否关注了房源
     *
     * @param userId
     * @param houseId
     * @return
     */
    @Override
    public int isFollowed(Long userId, Long houseId) {
        return userFollowDao.countByUserIdAndHouserId(userId, houseId);
    }

    /**
     * 取消关注房源
     *
     * @param id
     */
    @Override
    public void cancelFollow(Long id) {
        userFollowDao.delete(id);
    }


    @Reference
    private DictService dictService;

    /**
     * 我的关注
     *
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId) {
        PageHelper.startPage(pageNum, pageSize);
        Page<UserFollowVo> page = userFollowDao.findListPage(userId);
        List<UserFollowVo> list = page.getResult();
        for (UserFollowVo userFollowVo : list) {
            //户型：
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            //楼层
            String floorName = dictService.getNameById(userFollowVo.getFloorId());
            //朝向：
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            userFollowVo.setHouseTypeName(houseTypeName);
            userFollowVo.setFloorName(floorName);
            userFollowVo.setDirectionName(directionName);
        }
        return new PageInfo<UserFollowVo>(page, 10);
    }
}