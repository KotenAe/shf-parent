package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface UserFollowDao extends BaseDao<UserFollow> {
    //是否关注了房源
    int countByUserIdAndHouserId(@Param("userId") Long userId, @Param("houseId") Long houseId);
    //我的关注
    Page<UserFollowVo> findListPage(@Param("userId")Long userId);
}
