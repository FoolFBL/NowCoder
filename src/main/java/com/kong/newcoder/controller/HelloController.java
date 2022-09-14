package com.kong.newcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
/**
 * @author shijiu
 */
@Controller
@RequestMapping("/h1")
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello world";
    }

    @RequestMapping("/http")
    public void http(){

    }
    //get请求传参方式
    // /student?current=1
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current",required = false,defaultValue = "1")int current){
        System.out.println(current);
        return "ok";
    }

    // /student/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "ok";
    }

    //post请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String print(int value){
        System.out.println(value);
        return "ok";
    }

    //响应html数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("name","张三");
        mv.setViewName("/demo/view");
        return mv;
    }

    //响应json数据（异步请求）
    //java对象 - json字符串 -js对象
    @RequestMapping(path = "/emp" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        HashMap<String, Object> emp = new HashMap<>();
        emp.put("name","zhangsan");
        emp.put("age",23);
        return emp;
        //{"name":"zhangsan","age":23} 返回的是字符串类型
    }
}
