package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Resource
    private CommunityDao communityDao;
    @Resource
    private DictDao dictDao;

    @Override
    protected BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //使用工具类拿到封装在集合里面的pageNum和pageSize
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 5);
        //启用分页插件:
        PageHelper.startPage(pageNum, pageSize);
        //查询当前页的数据
        Page<Community> page = getEntityDao().findPage(filters);
        //根据areaId和plateId获取areaName和plateName
        List<Community> communityList = page.getResult();
        for (Community community : communityList) {
            String areaName = this.dictDao.getNameById(community.getAreaId());
            community.setAreaName(areaName);
            String plateName = this.dictDao.getNameById(community.getPlateId());
            community.setPlateName(plateName);
        }
        //封装数据并返回
        return new PageInfo<>(page, 10);
    }

    @Override
    public List<Community> findAll() {
        return this.communityDao.findAll();
    }

    /**
     * 重写getById方法
     * @param id
     * @return
     */
    @Override
    public Community getById(Serializable id) {
        //区域和板块对应着字典表里面的id，但这里有字典id，没有字典name。重写方法赋值
        Community  community = super.getById(id);
        //community对象里面封装着AreaId和PlateId，我们通过字典表里面的方法，在获取名字，给他赋值就可以了，根据字典id获取name
        String areaName = this.dictDao.getNameById(community.getAreaId());
        community.setAreaName(areaName);
        String plateName = this.dictDao.getNameById(community.getPlateId());
        community.setPlateName(plateName);

        return community;
    }
}
