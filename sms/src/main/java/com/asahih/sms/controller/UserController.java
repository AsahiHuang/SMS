package com.asahih.sms.controller;

import com.asahih.sms.pojo.Collect;
import com.asahih.sms.pojo.Comment;
import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.User;
import com.asahih.sms.result.Result;
import com.asahih.sms.service.*;
import com.asahih.sms.util.UuidTool;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/11/19.
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SubService subService;
    @Autowired
    PostService postService;
    @Autowired
    LogService logService;
    @Autowired
    CollectService collectService;
    @Autowired
    CommentService commentService;
    @Autowired
    JavaMailSender mailSender;



    // 修改密码或邮箱
    @CrossOrigin
    @PostMapping(value = "/api/changePwdOrEmail")
    @ResponseBody
    public Result updatePwdOrEmail(@RequestBody User requestUser) {
        User user = userService.getUserByUsername(requestUser.getUsername());
        if (requestUser.getPassword().length() != 0){
            String newPassword = requestUser.getPassword();
            String newSalt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2;
            String encodedPassword = new SimpleHash("md5", newPassword, newSalt, times).toString();
            user.setSalt(newSalt);
            user.setPassword(encodedPassword);
        } else if (requestUser.getEmail().length() != 0){
            String newEmail = requestUser.getEmail();
            user.setEmail(newEmail);
        }
        String name = (requestUser.getEmail().equals("")) ? "密码":"邮箱";
        logService.addLog(new Log("用户-修改信息",user.getUsername(),name, LocalDateTime.now().toString(),"修改成功","用户"));
        userService.addUser(user);
        return  new Result(200);
    }

    @GetMapping("/api/{username}/info")
    @CrossOrigin
    public User getUserInfo(@PathVariable("username") String username) throws Exception {
        User returnUser = userService.getUserByUsername(username);
        returnUser.setPassword("");
        returnUser.setSalt("");
        return  returnUser;
    }

    @GetMapping("/api/users")
    @CrossOrigin
    public List<User> list() throws Exception {
        List<User> users =   userService.list();
        for (User user : users){
            user.setPassword("");
            user.setSalt("");
        }
        return users;
    }
    // 修改权限（管理员） salt字段借用于表面执行者
    @PostMapping(value = "/api/user/updateAuth")
    @CrossOrigin
    @ResponseBody
    public User updateAuth(@RequestBody User requestUser) throws Exception {
        User user = userService.getUserByUsername(requestUser.getUsername());
        user.setAuth(!user.isAuth());
        userService.addUser(user);
        String logHandleSt = "修改为" +  ((user.isAuth())? "管理员":"用户");
        logService.addLog(new Log("用户-修改权限",requestUser.getUsername(),user.getUsername(), LocalDateTime.now().toString(),logHandleSt,"管理员:"+requestUser.getSalt()));
        return user;
    }

    // 重置密码（管理员）
    @PostMapping("/api/user/resetPwd")
    @CrossOrigin
    public User resetPwd(@RequestBody User requestUser) throws  Exception{
        String password = requestUser.getPassword();
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        User user = userService.getUserByUsername(requestUser.getUsername());
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        logService.addLog(new Log("用户-重置密码",requestUser.getUsername(),user.getUsername(), LocalDateTime.now().toString(),"重置成功","管理员:" + requestUser.getSalt()));
        userService.addUser(user);
        return user;
    }
    // 找回密码（用户）
    @PostMapping("/api/user/forgetPwd")
    @CrossOrigin
    public Result forgetPwd(@RequestBody User requestUser) throws Exception{
        User user = userService.getUserByUsername(requestUser.getUsername());
        if (user == null)
            return new Result(400);
        // UUID随机生成密码
        String password = UuidTool.generateShortUuid();
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        user.setPassword(encodedPassword);
        user.setSalt(salt);
        logService.addLog(new Log("用户-忘记密码",user.getUsername(),"密码",LocalDateTime.now().toString(),"重置成功","系统"));
        sendPwdMail(user,password);
        return new Result(200);

    }

    // 删除用户前要先删除他所有的个人订阅和多人订阅
    @PostMapping("/api/user/delete")
    @CrossOrigin
    public void deleteUser(@RequestBody User requestUser) throws Exception {

        String username = requestUser.getUsername();
        // 先删除个人订阅
        subService.deleteByUsername(username);
        // 删除多人订阅
        collectService.deleteAllByUsername(username);
        // 删除用户评论
        postService.deletePostByUsername(username);
        // 删除用户收藏
        commentService.deleteUserComment(username);
        //删除用户
        logService.addLog(new Log("用户-删除",requestUser.getUsername(),username, LocalDateTime.now().toString(),"注销成功","管理员:" + requestUser.getSalt()));
        userService.deleteUser(requestUser);
    }

    public void sendPwdMail(User user,String password) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("asahih@163.com");
        message.setTo(user.getEmail());
        message.setSubject("密码重置提醒");
        message.setText("你好 " + user.getUsername() +
                ":\n\n您的新密码是：" + password +
                "\n\n您现在可以使用上面的系统随机密码登录\n登录后请尽快在网站内：[管理中心]-[修改密码] 中更换该密码。\n\n\nFrom：订阅服务管理系统");
        mailSender.send(message);
        logService.addLog(new Log("用户-邮件提醒",user.getUsername(),"密码",LocalDateTime.now().toString(),"密码重置提醒","系统"));

    }

}
