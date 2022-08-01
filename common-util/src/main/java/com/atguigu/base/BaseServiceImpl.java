package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @InterfaceName:BaseServiceImpl
 * @Author: Kotena
 * @Dtae: 2022/7/20/20:52
 * @Description: 抽取的公共的BaseServiceImpl
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    protected abstract BaseDao<T> getEntityDao();

    public Integer insert(T t) {
        BaseDao<T> entityDao = getEntityDao();
        Integer insert = entityDao.insert(t);
        return insert;
    }

    public void delete(Long id) {
        getEntityDao().delete(id);
    }

    public Integer update(T t) {
        return getEntityDao().update(t);
    }

    public T getById(Serializable id) {
        return getEntityDao().getById(id);
    }

    public PageInfo<T> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<T>(getEntityDao().findPage(filters), 10);
    }
}
