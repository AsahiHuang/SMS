package com.asahih.sms.controller;

import com.asahih.sms.pojo.Collect;
import com.asahih.sms.pojo.Log;
import com.asahih.sms.result.Result;
import com.asahih.sms.service.CollectService;
import com.asahih.sms.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Created by Asahi Huang on 2020/12/13.
 */

@RestController
public class CollectController {
    @Autowired
    CollectService collectService;
    @Autowired
    LogService logService;

    // 添加/取消 收藏
    @PostMapping("/api/collect")
    @CrossOrigin
    public Result addOrChangeCollectCollected(@RequestBody Collect requestCollect) throws Exception {
        Collect collect = collectService.findByUsernameAndPid(requestCollect.getUsername(),requestCollect.getPost().getId());
        String st = "添加";
        int value = 200;
        if (collect != null){
            collectService.updateCollectCollected(collect);
            if (!collect.isCollected()){
                value = 400;
                st = "取消";
            }
        }
        else{
            requestCollect.setCollected(true);
            collectService.save(requestCollect);
        }
        logService.addLog(new Log("用户-" + st + "成功",requestCollect.getUsername(),requestCollect.getPost().getTitle(), LocalDateTime.now().toString(),st + "成功","用户"));
        return new Result(value);
    }


}
