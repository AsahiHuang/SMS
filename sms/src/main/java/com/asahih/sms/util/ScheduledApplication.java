package com.asahih.sms.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Asahi Huang on 2020/12/6.
 *
 *
 */


// @EnableScheduling注解的作用是发现注解@Scheduled的任务并后台执行。

@SpringBootApplication
@EnableScheduling
public class ScheduledApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
    }


}
