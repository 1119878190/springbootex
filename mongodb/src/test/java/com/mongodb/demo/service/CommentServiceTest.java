package com.mongodb.demo.service;

import com.mongodb.demo.domain.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void findList(){
        List<Comment> list = commentService.findList();
        System.out.println(list);
    }

    @Test
    public void save(){
        Comment comment = new Comment();
        comment.setArticleId("12312");
        comment.setContent("你好吗");
        comment.setParentId("33");
        comment.setLikeNum(123);
        commentService.save(comment);
    }

    @Test
    public void findByParentId(){
        Page<Comment> list = commentService.findCommentListByParentId("33", 1, 2);
        long totalElements = list.getTotalElements();
        System.out.println(totalElements);
    }

    @Test
    public void updateCommentLikeNum(){

        commentService.updateCommentLikeNum("618bc4baf2aa4c76e1c2f104");
    }
}
