package com.asahih.sms.service;

import com.asahih.sms.dao.CollectDAO;
import com.asahih.sms.dao.PostDAO;
import com.asahih.sms.pojo.Collect;
import com.asahih.sms.pojo.Post;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/13.
 */
@Service
public class CollectService {
    @Autowired
    CollectDAO collectDAO;
    @Autowired
    PostDAO postDAO;

    // 返回用户收藏的帖
    public List<Collect> listByUsernameAndCollected(String username){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return collectDAO.findAllByUsernameAndCollected(username,true,sort);
    }

    public Collect listByUsernameAndCollectedAndPost(String username,Post post){
        return collectDAO.findByUsernameAndCollectedAndPost(username,true,post);
    }

    public Collect findByUsernameAndPid(String username,int pid){
        Post post = postDAO.findById(pid);
        return  collectDAO.findByUsernameAndPost(username,post);
    }


    // 收藏或取消收藏
    public void updateCollectCollected(Collect collect){
        collect.setCollected(!collect.isCollected());
        this.save(collect);
    }

    public void save(Collect collect){
        collectDAO.save(collect);
    }

    @Transactional
    public void deleteAllByPost(Post post ){
        collectDAO.deleteAllByPost(post);
    }
    @Transactional
    public void deleteAllByUsername(String  username ){
        collectDAO.deleteAllByUsername(username);
    }

}
