package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {
    //将DictDao注入到容器
    @Autowired
    private DictDao dictDao;

    /**
     * 返回数据[{ id:2, isParent:true, name:"随意勾选 2"}]
     * 根据id获取子节点数据
     * 判断该节点是否是父节点
     * <p>
     * 获取子节点数据列表
     * 后台返回json数据，前台根据json构建一个页面
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> findZondes(Long id) {
        //1.调用Dao查询指定id的子元素列表
        List<Dict> dictList = dictDao.findListByParentId(id);
        //2.将List<Dict> 转换为前台（zTree）需要的Json格式的数据，List<Map>
        List<Map<String, Object>> zNodes = new ArrayList<>();
        //3.遍历 List<Dict> dictList，每一个元素是一个字典项，最终的数据要变成zTree需要的Json数据
        //[{ id:'01',	name:'n1',	isParent:true},
        // { id:'02',	name:'n2',	isParent:false},
        // { id:'03',	name:'n3',	isParent:true},
        // { id:'04',	name:'n4',	isParent:false}]
        for (Dict dict : dictList) {
            //创建一个Map
            Map<String, Object> map = new HashMap<>();
            //往集合里面放东西
            map.put("id", dict.getId());
            map.put("name", dict.getName());
            //使用三元运算符判断是不是父节点，是父节点往集合里面放，不是不放
            Integer count = this.dictDao.countIsParent(dict.getId());
            map.put("isParent", count > 0 ? true : false);
            zNodes.add(map);
        }
        return zNodes;
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        //根据dictCode找到DIct对象
        Dict dict = dictDao.findListByDictCode(dictCode);
        //调用dao里面的方法传递dictCode,dao里面得有接手参数的方法
        if (null == dict) return null;
        //获取id作为ParentId传递
        Long id = dict.getId();
        List<Dict> dictList = this.dictDao.findListByParentId(id);
        return dictList;
    }

    @Override
    public List<Dict> findListByParentId(Long parentId) {
        List<Dict> dictList = dictDao.findListByParentId(parentId);
        return dictList;
    }

    @Override
    public String getNameById(Long dictId) {
        return this.dictDao.getNameById(dictId);
    }

    @Override
    protected BaseDao<Dict> getEntityDao() {
        return this.dictDao;
    }
}
