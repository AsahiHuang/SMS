package com.asahih.sms.service;

import com.asahih.sms.dao.CommentDAO;
import com.asahih.sms.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/11.
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public List<Comment> listByPost(int pid){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return commentDAO.findAllByPid(pid,sort);
    }
    // 通过回复对象返回用户的通知
    public List<Comment> listByToUsername(String username){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return commentDAO.findAllByToUsername(username,sort);

    }
    // 通过用户名返回用户的评论
    public List<Comment> listByUsername(String username){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return commentDAO.findAllByUsername(username,sort);

    }

    public Comment findCommentById(int id){
        return commentDAO.findById(id);
    }

    public List<Comment> findAllByReviewId(int reviewId) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return commentDAO.findAllByReviewId(reviewId,sort);

    }

    public void addOrUpdate(Comment comment) {
        commentDAO.save(comment);
    }

    @Transactional
    public void deleteUserComment(String username) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        List<Comment> comments = commentDAO.findAllByUsername(username,sort);
        // 删除用户评论同时删除所有对话记录
        for (Comment comment : comments) {
            Comment newComment = commentDAO.findById(comment.getReviewId());
            if (newComment != null){
                newComment.setReviewId(0);
                newComment.setReviewText("");
                commentDAO.save(newComment);
            }
        }
        commentDAO.deleteAllByUsername(username);
    }

    public void deleteComment(int id) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Comment deleteComment = commentDAO.findById(id);
        List<Comment>  comments = commentDAO.findAllByReviewId(id,sort);
        for (Comment comment : comments) {
            Comment newComment = commentDAO.findById(comment.getId());
            newComment.setReviewId(0);
            newComment.setReviewText("");
            commentDAO.save(newComment);
        }
        commentDAO.delete(deleteComment);
    }

}
