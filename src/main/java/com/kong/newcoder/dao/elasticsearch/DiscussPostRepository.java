package com.kong.newcoder.dao.elasticsearch;

import com.kong.newcoder.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Repository;

/**
// * @author shijiu
 */
@Repository
@EnableElasticsearchRepositories
//实体类 主键类型
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {}
