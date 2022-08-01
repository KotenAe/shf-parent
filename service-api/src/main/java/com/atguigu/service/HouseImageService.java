package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;

import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {

    List<HouseImage> findImageList(Long id, int type);
}
