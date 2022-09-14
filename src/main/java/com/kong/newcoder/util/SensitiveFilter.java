package com.kong.newcoder.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.text.normalizer.Trie;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shijiu
 * 敏感词过滤器
 */
@Component
public class SensitiveFilter {
    private static  final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";
    //定义根节点
    private TrieNode rootNode = new TrieNode();
    //初始化
    @PostConstruct
    public void init(){


        try {
           InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
           //缓冲流
           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           String keyword;
           while((keyword=reader.readLine())!=null){
                //t添加到前缀树
               this.addKeyword(keyword);
           }

        } catch (Exception e) {
            logger.error("加载敏感词文件失败"+e.getMessage());
        }finally {

        }


    }

    //讲一个敏感词添加到前缀树中
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for(int i = 0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            //如果不存在
            if(subNode==null){
                //初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //只指向子节点 进入下一轮循环
            tempNode=subNode;
            //设置结束标识
            if(i==keyword.length()-1){
                tempNode.setKeyWordEnd(true);
            }

        }
    }

    //过滤敏感词之后的文本
    public String filter(String text){
        //判空
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1 指向树的根
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while(position<text.length()){
            char c = text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //若指针1处于根节点 将此符号计入结果 让指针2向下走一步
                if(tempNode==rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头或中间 指针3都向下走一步
                position++;
                continue;
            }
            //不是特殊符号
            //检查下级结点
            tempNode  = tempNode.getSubNode(c);
            if(tempNode==null){
                //以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position=++begin;
                //重新指向根节点
                tempNode=rootNode;
            }else if (tempNode.isKeyWordEnd()){
                //发现了敏感词 从begin-posotion字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin=++position;
                //重新指向根节点
                tempNode=rootNode;
            }else{
                //检查下一个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否为符号
    private boolean isSymbol(Character c){
        //东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }



    //定义前缀树结构 内部类
    private class TrieNode{
        //关键词结束的标识 即是否为叶子结点
        private boolean isKeyWordEnd;
        //子节点(key是下级字符 value是下级结点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
        //添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c, node);
        }
        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
