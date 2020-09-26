package cn.suxin.constant;

import cn.suxin.util.RandomUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class IniBean {
    
    private static AtomicInteger NUMBER_ID =new AtomicInteger(0); 
    
    
    public static String generId() {
        return "9"+(System.currentTimeMillis() +"").substring(8) +NUMBER_ID.getAndIncrement() +""+ Long.valueOf(RandomUtil.generateNumberString(4));
    }
}
