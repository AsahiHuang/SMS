package com.asahih.sms.util;

import com.asahih.sms.pojo.Log;
import com.asahih.sms.pojo.Sub;
import com.asahih.sms.pojo.User;
import com.asahih.sms.service.LogService;
import com.asahih.sms.service.SubService;
import com.asahih.sms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by Asahi Huang on 2020/12/6.
 */


@Component
public class ScheduledTask {

    @Autowired
    private UserService userService;
    @Autowired
    private SubService subService;
    @Autowired
    private LogService logService;
    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedRate = 5000)
    @PostConstruct
    public void deleteOverDataSub() throws Exception {
        List<Sub> subs = subService.list();
        for (Sub sub : subs){
            int dayDiff = timeDifference(sub.getOverDate());
            if (dayDiff <= 0) {
                logService.addLog(new Log("个人-自动删除", sub.getUser(), sub.getTitle(), LocalDateTime.now().toString(), "到期删除", "系统"));
                subService.deleteById(sub.getId());
                sendDeleteMail(sub);
            }
        }
    }

    // 提醒任务，cron表达式：每天八点执行一次
    @Scheduled(cron= "0 0 8 * * ? ")
    @PostConstruct
    public void notifyNearDataSub() throws Exception {
        List<Sub> subs = subService.list();
        for (Sub sub: subs) {
            int dayDiff = timeDifference(sub.getOverDate());
            if (dayDiff <=3) {
                logService.addLog(new Log("个人-自动提醒", sub.getUser(), sub.getTitle(), LocalDateTime.now().toString(), "临期提醒", "系统"));
                sendNotifyMail(sub);
            }
        }
    }

    public int timeDifference(Date endData) {
        /*天数差*/
        Date today = new Date();
        long from1 = today.getTime();
        long to1 = endData.getTime();
        int days = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        return days;
    }

    public void sendDeleteMail(Sub sub) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userService.getUserByUsername(sub.getUser());
        message.setFrom("asahih@163.com");
        message.setTo(user.getEmail());
        message.setSubject("订阅到期删除通知");
        message.setText("你好" + user.getUsername()
        + ":\n\n您在本网站记录的订阅服务："
        + sub.getTitle()
        + " 已到期，系统已自动删除订阅服务信息。"
        + "\n您可在网站内：[管理中心]-[操作记录]中查看相关记录。\n\n\n订阅服务管理系统");
        mailSender.send(message);
        logService.addLog(new Log("个人-邮件提醒",user.getUsername(),sub.getTitle(),LocalDateTime.now().toString(),"删除提醒","系统"));
    }


    public void sendNotifyMail(Sub sub) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        User user = userService.getUserByUsername(sub.getUser());
        message.setFrom("asahih@163.com");
        message.setTo(user.getEmail());
        message.setSubject("订阅即将到期提醒");
        message.setText("你好 " + user.getUsername() +
                ":\n\n您在本网站记录的订阅服务：" + sub.getTitle() +
                " 即将到期，到期后系统会自动删除该订阅服务信息。\n您可在网站内：[个人首页]或[管理中心]-[个人订阅]中查看更多。\n\n\nFrom：订阅服务管理系统");
        mailSender.send(message);
        logService.addLog(new Log("个人-邮件提醒",user.getUsername(),sub.getTitle(),LocalDateTime.now().toString(),"临期提醒","系统"));
    }
}




