package com.asahih.sms.util;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Asahi Huang on 2020/12/6.
 */
public class TimeDifference {
    public static int timeDifference(Date endData) throws ParseException {
        /*天数差*/
        Date today = new Date();
        long from1 = today.getTime();
        long to1 = endData.getTime();
        int days = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        System.out.println("两个时间之间的天数差为：" + days);
        return days;
    }
}
