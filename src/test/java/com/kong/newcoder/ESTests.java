package com.kong.newcoder;

import com.kong.newcoder.dao.DiscussPostMapper;
import com.kong.newcoder.dao.UserMapper;
import com.kong.newcoder.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author shijiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class ESTests {
    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private ElasticsearchRepository repository;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    //一条条添加数据

    @Test
    public void testInsert(){
        try{
            System.out.println("ok");
            repository.save(discussPostMapper.selectDiscussPostById(241));
            repository.save(discussPostMapper.selectDiscussPostById(242));
            repository.save(discussPostMapper.selectDiscussPostById(243));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    //一次添加多条数据
    @Test
    public void testInsertList(){
        repository.saveAll(discussPostMapper.selectAllDiscussPosts());
    }

    //修改数据 localhost:9200/discusspost/_doc/231
    @Test
    public void updataList(){
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(231);
        discussPost.setContent("lallalalalalala");
        repository.save(discussPost);
    }


    //删除数据
    @Test
    public void testDelete(){
        repository.deleteById(231);
//        repository.deleteAll();
    }

    @Test
    //搜索
    public void testSearch(){
        //构造查询条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //从title和content中搜 搜索条件
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                //排序方式
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createtime").order(SortOrder.DESC))
                //分页
                .withPageable(PageRequest.of(0, 10))
                //高亮显示
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        SearchHits<DiscussPost> search = template.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
//        for (SearchHit<DiscussPost> searchHit : searchHits) {
//            System.out.println(searchHit);
//        }
//        System.out.println(searchHits.size());
        //设置一个最后需要返回的实体类集合
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
        for (DiscussPost post : posts) {
            System.out.println(post);
        }
    }
}