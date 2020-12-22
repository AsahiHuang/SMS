package com.asahih.sms.dao;

import com.asahih.sms.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/11.
 */
public interface CommentDAO extends JpaRepository<Comment,Integer> {
    List<Comment> findAllByPid(int pid, Sort sort);
    List<Comment> findAllByToUsername(String username, Sort sort);
    List<Comment> findAllByUsername(String username, Sort sort);
    List<Comment> findAllByReviewId(int reviewId,Sort sort);
    List<Comment> deleteAllByUsername(String username);
    Comment findById(int it);

}
