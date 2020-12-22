package com.asahih.sms.dao;

/**
 * Created by Asahi Huang on 2020/9/15.
 *
 */
import com.asahih.sms.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    List<User> findAllByUsernameLike(String username);

    User save(User user);

}
