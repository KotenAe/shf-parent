package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

public interface DictService {
    /**
     * 数据字典功能实现
     * 定义findZones接口,用于查询指定id的下级元素
     *
     * @param id
     * @return 从数据库里面查询的是List<Dict>，但我们的返回值不是List<Dict>，而是List<Map>已经根据前台需要的json格式进行了转换
     */

    List<Map<String, Object>> findZondes(Long id);
    /**
     * 小区管理功能实现，搜索界面选择区域显示
     * 根据编码获取子节点数据列表
     * @param dictCode
     * @return
     */
    List<Dict> findListByDictCode(String dictCode);

    List<Dict> findListByParentId(Long parentId);
    //根据id查询name
    String getNameById(Long dictId);
}
