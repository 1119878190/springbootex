package com.mongodb.demo.dao;

import com.mongodb.demo.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentDao extends MongoRepository<Comment,String> {

    Page<Comment> findByParentId(String parentId, Pageable pageable);
}
