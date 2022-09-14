package com.kong.newcoder.controller;

import com.kong.newcoder.util.CommunityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shijiu
 */
@RestController
public class TestController {

    @RequestMapping("/h1")
    public String hello(){

        return "hello sbhr";
    }

    //ajax示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String Ajaxdemo(String name,int age){
        System.out.println(name);
        System.out.println(age);
        Map<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("age",age);
        return CommunityUtil.getJSONString(0,"操作成功",map);

    }


}
