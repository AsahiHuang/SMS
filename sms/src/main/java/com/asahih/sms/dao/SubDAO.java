package com.asahih.sms.dao;

import com.asahih.sms.pojo.Category;
import com.asahih.sms.pojo.Sub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/10/13.
 */
public interface SubDAO extends JpaRepository<Sub,Integer> {
    List<Sub> findAllByCategory(Category category);
    List<Sub> findAllByCategoryAndUser(Category category,String username);
    List<Sub> findAllByUser(String username);
    List<Sub> findAllByUserAndTitleLike(String username,String title);
}
