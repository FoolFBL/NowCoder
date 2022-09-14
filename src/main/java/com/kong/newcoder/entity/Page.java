package com.kong.newcoder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author shijiu
 * 封装分页相关信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    //当前的页码
    private int current = 1;
    //显示条数 上限
    private  int limit = 10;
    //数据总数 用于计算总页数 总数/limit
    private int rows;
    //查询路径(每个小页码对应的连接 复用)
    private String path;

    public void setCurrent(int current) {
        if(current>=1){
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if(limit>=1&&limit<=100){
            this.limit = limit;
        }
    }
    //获取当前页的起始行
    public int getOffset(){
        return (current-1)*limit;
    }

    //获取总页数
    public int getTotal(){
        if(rows%limit==0){
            return rows/limit;
        }
        return rows/limit+1;
    }
    //获取起始页码
    public int getFrom(){
        int from = current - 2;
        return max(from,1);
    }

    //获取终止页码
    public int getTo(){
        int to = current+2;
        int total = getTotal();
        return min(to,total);
    }

}
