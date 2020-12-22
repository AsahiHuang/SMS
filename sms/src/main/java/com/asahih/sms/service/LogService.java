package com.asahih.sms.service;

import com.asahih.sms.dao.LogDAO;
import com.asahih.sms.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/6.
 */
@Service
public class LogService {
    @Autowired
    LogDAO logDAO;

    // 管理员输出全部日志
    public List<Log> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return logDAO.findAll(sort);
    }

    // 用户输出自己的记录
    public List<Log> listByUsername(String username) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return logDAO.findAllByUsername(username,sort);
    }

    public void addLog(Log log) {logDAO.save(log);}

}
