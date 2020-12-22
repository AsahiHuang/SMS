package com.asahih.sms.dao;

import com.asahih.sms.pojo.Collect;
import com.asahih.sms.pojo.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/13.
 */
public interface CollectDAO  extends JpaRepository<Collect,Integer> {
    List<Collect> findAllByUsernameAndCollected(String username, boolean collected, Sort sort);
    Collect save(Collect collect);
    Collect findByUsernameAndPost(String username,Post post);
    Collect findByUsernameAndCollectedAndPost(String username, boolean collected, Post post);
    List<Collect> deleteAllByPost(Post post);
    List<Collect> deleteAllByUsername(String username);

}
