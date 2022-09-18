package com.kong.newcoder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableElasticsearchRepositories
public class NewCoderApplication {
//
//    @PostConstruct
//    public void init(){
//        //解决netty启动冲突问题
//        System.setProperties("es.set.netty.runtime.available.processors","false");
//
//    }

    public static void main(String[] args) {
        //启动tomcat 自动创建spring容器 装载bean
        try {
            SpringApplication.run(NewCoderApplication.class, args);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
