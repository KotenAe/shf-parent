package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {
    //使用dubbo提供的注解方式，注入容器
    @Reference
    private DictService dictService;
    //定义返回页面
    private final static String PAGE_INDEX = "dict/index";

    //前台超链接页面的跳转
    @RequestMapping
    public String index() {
        return PAGE_INDEX;
    }

    /**
     * 如果返回值是List<Map<String, Object>> zondes ，那么将会只返回数据
     * 返回值是：Result, 里面封装了响应状态码，还封装了List<Map<String, Object>> zondes
     * SUCCESS(200,"成功"),
     * FAIL(201, "失败"),
     * PARAM_ERROR(203, "参数错误"),
     * DATA_ERROR(204, "数据异常"),
     * ILLEGAL_REQUEST(205, "非法请求"),
     * REPEAT_SUBMIT(206, "重复提交"),
     */
    @RequestMapping("/findZnodes")
    //发送ajax请求，建议用result封装
    @ResponseBody
    //使用@RequestParam(value = "id",defaultValue = "0"，绑定前端页面的id，因为我们是parentId，而且前端没有默认值，我们在这里设置一个默认值
    public Result findZondes(@RequestParam(value = "id", defaultValue = "0") Long parentId) {

        List<Map<String, Object>> zondes = this.dictService.findZondes(parentId);
        //我们不希望返回znodes，而是想讲结果封装到
        return Result.ok(zondes);
    }

    /**
     * 根据DictCode获取子节点列表，并把它封装到Result中
     *
     * @param dictCode
     * @return
     */
    @RequestMapping("/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode) {
        List<Dict> dictList = this.dictService.findListByDictCode(dictCode);
        return Result.ok(dictList);
    }

    /**
     * 根据上级id获取子节点数据列表
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
        List<Dict> dictList = this.dictService.findListByParentId(parentId);
        return Result.ok(dictList);
    }
}