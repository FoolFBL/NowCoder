package com.kong.newcoder.Service;

import com.kong.newcoder.dao.elasticsearch.DiscussPostRepository;
import com.kong.newcoder.entity.DiscussPost;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository repository;
    @Autowired
    private ElasticsearchRestTemplate template;
    //存帖子
    public void saveDiscussPost(DiscussPost post){
      template.save(post);
//        repository.save(post);
    }
    //删除
    public void deleteDiscussPost(int id){
      template.delete(String.valueOf(id),DiscussPost.class);
//        repository.deleteById(id);
    }

    public List<DiscussPost> searchDiscussPost(String keyword, int current, int limit){
        //构造查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //从title和content中搜 搜索条件
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                //排序方式
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createtime").order(SortOrder.DESC))
                //分页
                .withPageable(PageRequest.of(current, limit))
                //高亮显示
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        SearchHits<DiscussPost> search = template.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        System.out.println(searchHits.size()+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "kkkkkkkk");
        List<DiscussPost> posts = new ArrayList<>();
        //遍历返回的内容进行处理
        for(SearchHit<DiscussPost> searchHit:searchHits){
            //高亮的内容
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            //将高亮的内容填充到content中
            searchHit.getContent().setTitle(highlightFields.get("title")==null ? searchHit.getContent().getTitle():highlightFields.get("title").get(0));
            searchHit.getContent().setContent(highlightFields.get("content")==null ? searchHit.getContent().getContent():highlightFields.get("content").get(0));
            //放到实体类中
            posts.add(searchHit.getContent());
        }
        return posts;
    }}