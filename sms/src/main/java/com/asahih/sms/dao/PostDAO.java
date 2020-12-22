package com.asahih.sms.dao;

import com.asahih.sms.pojo.Category;
import com.asahih.sms.pojo.Post;
import com.asahih.sms.pojo.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by Asahi Huang on 2020/10/25.
 */
public interface PostDAO extends JpaRepository<Post,Integer> {
    Post findById(int id);
    List<Post> findAllByCategory(Category category);
    List<Post> findAllByAvailable(boolean available, Sort sort);
    List<Post> findAllByIdAndAvailable(int id,boolean available);
    List<Post> findAllByCategoryAndAvailable(Category category,boolean available,Sort sort);
    List<Post> findAllByTitleLike(String title,Sort sort);
    List<Post> findAllByTitleLikeAndAvailable(String title,boolean available,Sort sort);
    List<Post> findAllByUserAndTitleLikeAndAvailable(User user,String title,boolean available,Sort sort);
    List<Post> findAllByUser(User user,Sort sort);
    List<Post> findAllByUserAndAvailable(User user,boolean available,Sort sort);

}
