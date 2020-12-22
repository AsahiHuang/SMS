package com.asahih.sms.dao;

import com.asahih.sms.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Asahi Huang on 2020/10/13.
 */
public interface CategoryDAO extends JpaRepository<Category, Integer> {

}