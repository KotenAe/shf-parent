package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Dict;

import java.util.List;

public interface DictDao extends BaseDao<Dict> {
    List<Dict> findListByParentId(Long parentId);

    //判断一个节点的子节点数量，从而判断是不是父节点
    Integer countIsParent(Long id);

    String getNameById(Long id);

    Dict findListByDictCode(String dictCode);
}
