package cn.suxin.constant;

public class Constant {
    
    public final static String RET_CODE = "retCode";
    public final static String RET_DESC = "retDesc";
    public final static String RET_DETAIL = "retDetail";
    
    public final static String RET_USERID = "userId";
    
    //有效期
    public final static Long LOGIN_OUT_TIME = 24 * 60 * 60 * 1000l;
    
    //http://epaper.jwb.com.cn/jwb/html/2018-07/23/node_1.htm
    public final static String JWB_URL = "http://epaper.jwb.com.cn/jwb/html/%s/%s/%s";
    
    
    public final static int TASK_INI = 0;
    public final static int TASK_RUNNING = 1;
    public final static int TASK_END_SUCCESS = 9;
    public final static int TASK_END_FAIL = -1;
    
    public final static String CACHE_TASK_LIST = "tasklist";
    public final static String CACHE_PAGE_LIST_PRE = "page:"; //page_2018-15-12
    public final static String CACHE_ARCTICLE_HASH = "art:hash";
    
    public final static String CACHE_PRINTARTLIST_ZSET = "printartlist";
    
    public final static String UA = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
    public final static String FK = "副刊";
    
    public final static int TASK_TYPE_PAGE = 1;
    public final static int TASK_TYPE_ACTICLE = 2;
    public final static int TASK_TYPE_PRINT = 3;


    public final static String CACHE_XMWB_ART_HASH = "xmwb:art:hash";
    public final static String CACHE_XMWB_PRINTARTLIST_ZSET = "xmwb:printartlist";

}
