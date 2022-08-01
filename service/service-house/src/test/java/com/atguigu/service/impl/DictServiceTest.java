package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.service.DictService;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;

//使用Junit5联合Spring进行测试，需要加上@SpringJUnitConfig注解
@SpringJUnitConfig(locations = {"classpath:spring/spring-*xml"})
public class DictServiceTest {
    //注入属性
    @Reference
    private DictService dictService;

    @Test
    public void testFindZnodes() {
        List<Map<String, Object>> zondes = this.dictService.findZondes(0L);
        zondes.forEach(System.out::println);
    }

}