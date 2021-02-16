package cn.suxin.util;

import cn.suxin.constant.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SpiderUtil {

    private final static int TIMEOUT = 5000;

    public static Document getDocment(String queryUrl){
        Document doc = null;
        int times = 1;
        int timeOut = 5000;
        while (times < 5) {
            try {
                doc = Jsoup.connect(queryUrl).header("User-Agent", Constant.UA).timeout(timeOut).get();
                break;
            } catch (Exception e) {
                times++;
                timeOut = timeOut + 3000;
            }
        }
        return doc;
    }

}
