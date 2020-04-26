package com.netent.bookstore.repository;

import com.netent.bookstore.model.MediaPost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MediaPostRepository extends CrudRepository<MediaPost, Long> {

    List<MediaPost> findMediaPostsByTitleIgnoreCaseContainingOrBodyIgnoreCaseContaining(String title, String body);

}
