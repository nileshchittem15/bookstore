package com.netent.bookstore.repository.elasticsearch;

import com.netent.bookstore.model.MediaPost;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MediaPostRepository extends ElasticsearchRepository<MediaPost, String> {

    @Query("{ "
        + "\"query_string\":{"
                + "\"query\":\"?0\","
                + "\"fields\": [\"title\", \"body\"]"
            +"}"
        +"}")
    List<MediaPost> findMediaPosts(String title);

}
