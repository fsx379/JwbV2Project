package cn.suxin.sevice;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import cn.suxin.constant.Constant;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.FKInfo;
import cn.suxin.model.PageInfo;
import cn.suxin.util.JsonUtils;

public class JwbSpiderUtil {

    public static String getURL(String firstP, String secondP, String thirdP) {
        return String.format(Constant.JWB_URL, firstP, secondP, thirdP);
    }


    /**
     * 获取版面信息
     * 
     * @param url
     */
    public static void queryPageInfo(String url, PageInfo pageInfo, String firstP, String secondP) {
        try {
            Document doc = null;

            int times = 1;
            int timeOut = 5000;
            long startTime = System.currentTimeMillis();
            while (times < 5) {
                try {
                    doc = Jsoup.connect(url).header("User-Agent", Constant.UA).timeout(timeOut).get();
                    break;
                } catch (Exception e) {
                	times++;
                    timeOut = timeOut + 3000;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("[getArtTime] times=" + times +" ,spend=" + (endTime - startTime) +"ms");
            
            Element element = doc.getElementById("bmdhTable");
            Elements titles = element.select(".rigth_bmdh_href");
            if (titles != null && titles.size() > 0) {

                for (Element tmp : titles) {
                    if (tmp.text().replaceAll(" ", "").contains(Constant.FK)) {
                        FKInfo fk = new FKInfo(pageInfo.getPageDate());
                        fk.setPageUrl(getURL(firstP, secondP, tmp.attr("href")));
                        fk.setPageDesc(tmp.text());
                        pageInfo.addFKInfo(fk);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[queryPageInfo] error ! " + url + " " + e.getMessage());
            e.printStackTrace();

        }
    }
    
    /**
     * 仅仅查询文章列表
     * @param pageInfo
     */
    public static void queryArticleList(PageInfo pageInfo , String firstP, String secondP) {
        
        Document doc = null;
        List<FKInfo> fkList = pageInfo.getList();
        
        try {
            for(FKInfo fk : fkList) {
                int i = 3;
                while (i > 0) {
                    try {
                        doc = Jsoup.connect(fk.getPageUrl()).header("User-Agent", Constant.UA).timeout(3000).get();
                        i = -1;
                    } catch (Exception e) {
                        i--;
                    }
                }
                
                Element element = doc.getElementById("main-ed-articlenav-list");
                Elements tmps = element.getElementsByTag("a");
                for(Element tmp : tmps){
                    String url = tmp.attr("href");
                    String title = tmp.text();
                    ArticleInfo art = new ArticleInfo(fk);
                    art.setArtTitle(title);
                    art.setArtUrl(getURL(firstP, secondP, url));
                    
                    fk.addArtList(art);
                }
                
                
                
            }
            
        } catch (Exception e) {
            System.out.println("[queryArticleList] error ! " + JsonUtils.toJson(pageInfo)  );
            e.printStackTrace();

        }
        
    }
    
    /**
     * 文章内容爬取
     * @param url
     */
    public static void queryArticleDetal(ArticleInfo artInfo) {
        try {
            
            Document doc = null;
            int i = 3;
            while (i > 0) {
                try {
                    doc = Jsoup.connect(artInfo.getArtUrl()).header("User-Agent", Constant.UA).timeout(5000).get();
                    i = -1;
                } catch (Exception e) {
                    i--;
                }
            }
            
            Elements title = doc.getElementsByTag("founder-title");
            if(title != null && title.size() > 0){
                artInfo.setArtTitle(title.get(0).text());
            }
            Elements author = doc.getElementsByTag("founder-author");
            if(author != null && author.size() > 0){
                String tmp = author.text();
                if(StringUtils.isEmpty(tmp)) {
                    tmp = title.get(0).parent().parent().nextElementSibling().text();
                }
                artInfo.setArtAhthor(tmp );
            }else {
                artInfo.setArtAhthor(title.get(0).parent().parent().nextElementSibling().text());
            }
            
            Elements content = doc.getElementsByTag("founder-content");
            if(content != null && content.size() > 0){
                artInfo.setArtContent(content.text());
            }
            
        } catch (Exception e) {
            System.out.println("[queryArticleDetal] error ! " + JsonUtils.toJson(artInfo)  );
            e.printStackTrace();
        }
    }
    
    
}
