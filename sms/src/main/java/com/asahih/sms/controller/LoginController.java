package com.asahih.sms.controller;

import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.User;
import com.asahih.sms.result.Result;
import com.asahih.sms.service.LogService;
import com.asahih.sms.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

/**
 * Created by Asahi Huang on 2020/10/12.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    LogService logService;


    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        username = HtmlUtils.htmlEscape(username);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        User user = userService.getUserByUsername(username);
        if (user != null){
            String st = (user.isAuth()) ?"管理员":"用户";
            try {
                subject.login(usernamePasswordToken);
                logService.addLog(new Log("用户-登录",username,username, LocalDateTime.now().toString(),"登录成功",st));
                return new Result(200);
            } catch (AuthenticationException e) {
                logService.addLog(new Log("用户-登录",username,username, LocalDateTime.now().toString(),"登录失败",st));
                return new Result(400);
            }
        }
        return new Result(400);
    }

}
