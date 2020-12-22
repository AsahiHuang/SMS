package com.asahih.sms.service;

/**
 * Created by Asahi Huang on 2020/9/15.
 */
import com.asahih.sms.dao.UserDAO;
import com.asahih.sms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDao;
    public List<User> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return userDao.findAll(sort);
    }

    public User getUserByUsername(String username){
        return userDao.findByUsername(username);
    }

    public boolean isExist(String username){
        User user = getUserByUsername(username);
        return user!=null;
    }

    public void addUser(User user){
        userDao.save(user);
    }

    public void deleteUser(User user){
        userDao.deleteById(user.getId());
    }
    public List<User> searchUserByUsername(String username){
        return userDao.findAllByUsernameLike(username);
    }
}
