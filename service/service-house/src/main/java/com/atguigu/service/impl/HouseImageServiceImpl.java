package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.HouseImageDao;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.HouseImageService;

import javax.annotation.Resource;
import java.util.List;
@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {
    @Resource
    private HouseImageDao houseImageDao;
    @Override
    protected BaseDao<HouseImage> getEntityDao() {
        return houseImageDao;
    }

    @Override
    public List<HouseImage> findImageList(Long id, int type) {
        return houseImageDao.findImageList(id,type);
    }
}
