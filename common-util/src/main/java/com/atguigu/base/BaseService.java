package com.atguigu.base;


import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @InterfaceName:BaseService
 * @Author: Kotena
 * @Dtae: 2022/7/20/20:48
 * @Description: 抽取的公共的BaseService
 */
public interface BaseService<T> {

    //新增
    Integer insert(T t);

    //根据id查询，用于回显
    T getById(Serializable id);

    //修改
    Integer update(T t);

    //删除
    void delete(Long id);

    //分页
    PageInfo<T> findPage(Map<String, Object> filters);
}
