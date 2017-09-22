package com.here.util;


/**
 * Created by hyc on 2017/7/5 22:26
 */

public class TimeUtil {

    public static int countMinute(String start,String over){
        return (Integer.parseInt(over.split(":")[0])-Integer
                .parseInt(start.split(":")[0]))*60+(Integer
                .parseInt(over.split(":")[1])-Integer.parseInt(start.split(":")[1]));
    }




}
