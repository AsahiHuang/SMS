package com.asahih.sms.controller;

import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.Sub;
import com.asahih.sms.service.LogService;
import com.asahih.sms.service.SubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/10/13.
 */
@RestController
public class SubController {
    @Autowired
    SubService subService;
    @Autowired
    LogService logService;


    @GetMapping("/api/subs")
    @CrossOrigin
    public List<Sub> list() throws Exception {
        return subService.list();
    }

    @PostMapping("/api/subs")
    @CrossOrigin
    public Sub addOrUpdate(@RequestBody Sub sub) throws Exception {

        logService.addLog(new Log("个人-添加/修改",sub.getUser(),sub.getTitle(), LocalDateTime.now().toString(),"添加/修改成功","用户"));
        subService.addOrUpdate(sub);
        return sub;
    }

    @PostMapping("/api/delete")
    @CrossOrigin
    public void delete(@RequestBody Sub sub) throws Exception {
        logService.addLog(new Log("个人-删除",sub.getUser(),sub.getTitle(), LocalDateTime.now().toString(),"删除成功","用户"));
        subService.deleteById(sub.getId());
    }


    @GetMapping("/api/categories/{cid}/subs")
    @CrossOrigin
    public List<Sub> listByCategory(@PathVariable("cid") int cid) throws Exception {
        if (0 != cid) {
            return subService.listByCategory(cid);
        } else {
            return list();
        }
    }

    // 通过用户的查询方法
    @GetMapping("/api/{user}/categories/{cid}/subs")
    @CrossOrigin
    public List<Sub> listByCategoryAndUser(@PathVariable("cid") int cid,@PathVariable("user") String username) throws Exception {
        if (0 != cid) {
            return subService.listByCategoryAndUser(cid,username);
        } else {
            return listByUser(username);
        }
    }

    @GetMapping("/api/{user}/subs")
    @CrossOrigin
    public List<Sub> listByUser(@PathVariable("user") String username) throws Exception {
        return subService.listByUser(username);
    }

    @CrossOrigin
    @GetMapping("/api/{user}/search")
    public List<Sub> searchResult(@PathVariable("user") String username,@RequestParam("keywords") String keywords) {
        // 关键词为空时查询该用户所有订阅
        if ("".equals(keywords)) {
            return subService.listByUser(username);
        } else {
            return subService.Search(username,keywords);
        }
    }



}
