package com.asahih.sms.controller;

import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.User;
import com.asahih.sms.result.Result;
import com.asahih.sms.service.LogService;
import com.asahih.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/7.
 */
@RestController
public class LogController {
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/log/logout")
    @ResponseBody
    public Result logout(@RequestBody User requestUser){
        String username = requestUser.getUsername();
        logService.addLog(new Log("用户-登出",username,username, LocalDateTime.now().toString(),"登出成功","用户/管理员"));
        return new Result(200);
    }

    // 查询记录
    @GetMapping("/api/{username}/logs")
    @CrossOrigin
    public List<Log> listByUsername(@PathVariable("username") String username) throws Exception{
        User user = userService.getUserByUsername(username);
        if (user.isAuth())
            return logService.list();
        return logService.listByUsername(username);
    }


}
