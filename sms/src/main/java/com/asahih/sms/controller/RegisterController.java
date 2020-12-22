package com.asahih.sms.controller;

import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.User;
import com.asahih.sms.result.Result;
import com.asahih.sms.service.LogService;
import com.asahih.sms.service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
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
public class RegisterController {
    @Autowired
    UserService userService;
    @Autowired
    LogService logService;

    @CrossOrigin
    @PostMapping(value = "/api/register")
    @ResponseBody
    public Result register(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        String password = requestUser.getPassword();
        String email = requestUser.getEmail();

        // 判断用户名是否存在
        boolean exist = userService.isExist(username);
        if (exist)
            // 存在则返回错误码 400
            return new Result(400);
        User newUser = new User();
        newUser.setUsername(username);
        // 生成盐,默认长度 16 位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        newUser.setSalt(salt);
        newUser.setPassword(encodedPassword);
        newUser.setEmail(email);
        userService.addUser(newUser);
        logService.addLog(new Log("用户-注册",newUser.getUsername(),"", LocalDateTime.now().toString(),"注册成功","用户"));
        return new Result(200);
    }
}
