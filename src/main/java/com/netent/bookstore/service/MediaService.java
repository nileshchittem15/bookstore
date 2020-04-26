package com.netent.bookstore.service;

import com.netent.bookstore.constants.ErrorMessages;
import com.netent.bookstore.exception.BookStoreException;
import com.netent.bookstore.model.MediaPost;
import com.netent.bookstore.repository.elasticsearch.MediaPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    @Value("${book.mediaPost.url}")
    private String mediaPostUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MediaPostRepository mediaPostRepository;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000, initialDelay=24 * 60 * 60 * 1000)
    public void updateMediaPosts(){

        List<MediaPost> mediaPostList = fetchMediaPosts();

        if(mediaPostList==null || mediaPostList.isEmpty()){
            logger.warn(ErrorMessages.NO_MEDIA_POSTS_FOUND);
        }

        for(MediaPost mediaPost: mediaPostList){
            try {
                mediaPostRepository.save(mediaPost);
            }catch (ElasticsearchException e){
                logger.error(ErrorMessages.ES_EXCEPTION, e);
                throw new BookStoreException(ErrorMessages.ES_EXCEPTION,
                    HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    public List<String> getMediaPostsTitles(String title){

        List<String> postTitleList = new ArrayList<>();
        title = "*"+title+"*";
        try {
            List<MediaPost> mediaPostList = mediaPostRepository.findMediaPosts(title);
            for(MediaPost mediaPost:mediaPostList){
                postTitleList.add(mediaPost.getTitle());
            }
        }catch (ElasticsearchException e){
            logger.error(ErrorMessages.ES_EXCEPTION, e);
            throw new BookStoreException(ErrorMessages.ES_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return postTitleList;
    }

    private List<MediaPost> fetchMediaPosts(){

        List<MediaPost> mediaPostList;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<MediaPost>> rateResponse =
                restTemplate.exchange(mediaPostUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<MediaPost>>() {
                });
            mediaPostList = rateResponse.getBody();
            return mediaPostList;
        }catch (HttpStatusCodeException e){
            logger.error(ErrorMessages.MEDIA_CLIENT_EXCEPTION,e);
            throw new BookStoreException(ErrorMessages.MEDIA_CLIENT_EXCEPTION,e.getStatusCode());
        }
    }


}
