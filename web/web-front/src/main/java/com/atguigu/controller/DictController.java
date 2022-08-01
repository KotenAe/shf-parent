package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//所有的请求都是异步请求，所以加上@RestController
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    //注入DictService
    @Reference
    private DictService dictService;

    //根据编码获取子节点
    @GetMapping("/findListByDictCode/{dictCode}")
    public  Result<List<Dict>>  findListByDictCode(@PathVariable String dictCode) {
        //调用业务逻辑层的DictService根据编码获取子节点的方法，findListByDictCode
        List<Dict> listByDictCode = dictService.findListByDictCode(dictCode);
        //ok里面装的(data)
        return Result.ok(listByDictCode);
    }
    /**
     * 根据上级id获取子节点数据列表
     * @param parentId
     * @return
     */
    @RequestMapping ("findListByParentId/{parentId}")
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }
}

