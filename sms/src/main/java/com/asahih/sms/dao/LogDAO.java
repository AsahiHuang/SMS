package com.asahih.sms.dao;

import com.asahih.sms.pojo.Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/6.
 */
public interface LogDAO extends JpaRepository<Log,Integer> {

    List<Log> findAllByUsername(String username,Sort sort);
}
