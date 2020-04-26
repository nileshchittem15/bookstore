package com.netent.bookstore.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "media_post", type = "MediaPost")
public class MediaPost {

    @Id
    private String id;

    private String userId;

    private String body;

    private String title;

}
