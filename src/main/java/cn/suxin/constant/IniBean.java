package cn.suxin.constant;

import java.util.concurrent.atomic.AtomicInteger;

public class IniBean {
    
    private static AtomicInteger NUMBER_ID =new AtomicInteger(0); 
    
    
    public static String generId() {
        return System.currentTimeMillis() + ""+ NUMBER_ID.getAndIncrement();
    }
}
