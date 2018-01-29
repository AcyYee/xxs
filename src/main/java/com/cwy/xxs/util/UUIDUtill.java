package com.cwy.xxs.util;

import java.util.UUID;

public class UUIDUtill {
    public static String getOrderTag(String storeKey) {
        UUID uuid = UUID.randomUUID();
        StringBuilder builder = new StringBuilder();
        builder.append(storeKey);
        builder.append(TimeUtil.getDateTime(5));
        String less = uuid.toString().replace("-","").substring(0,4);
        builder.append(less);
        return builder.toString();
    }

    public static String getUUID(int count) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","").substring(0,count);
    }

    public static String getCode(){
        return String.valueOf((Math.random()*9+1)*100000);
    }

}
