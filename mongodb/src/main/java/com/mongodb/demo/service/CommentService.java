package com.mongodb.demo.service;

import com.mongodb.demo.dao.CommentDao;
import com.mongodb.demo.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Comment comment){
        commentDao.save(comment);
    }

    public List<Comment> findList(){
        return commentDao.findAll();
    }

    public Page<Comment> findCommentListByParentId(String parentId,int page,int size){
         return commentDao.findByParentId(parentId, PageRequest.of(page,size));
    }

    public void updateCommentLikeNum(String id){
        Query query = Query.query(Criteria.where("_id").is(id).and("parentId").is("33"));
        Update update = new Update().set("likeNum",1000).set("content","mongoTemplateTest");
        mongoTemplate.updateFirst(query,update,Comment.class);

    }
}
