package cn.suxin.sevice;

import cn.suxin.constant.Constant;
import cn.suxin.constant.XmwbConstant;
import cn.suxin.model.XmwbArticleVo;
import cn.suxin.model.XmwbChapterVo;
import cn.suxin.redis.RedisService;
import cn.suxin.threadpool.TaskThreadPool;
import cn.suxin.threadpool.XmwbPrintWordThread;
import cn.suxin.util.DateUtil;
import cn.suxin.util.SpiderUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("xmwbService")
public class XmwbServiceImp implements XmwbService{

    private static Logger logger = LoggerFactory.getLogger(XmwbServiceImp.class);

    @Autowired
    RedisService redisService;

    @Override
    public List<XmwbChapterVo> getChapterLists(Date queryDate) {
        String queryDateStr = DateUtil.formatDate(queryDate,DateUtil.DEFAULT_DATE_FORMAT);
        String queryUrl = String.format(XmwbConstant.URL_CHAPTER_DEF , queryDateStr );
        List<XmwbChapterVo>  retList = new ArrayList<XmwbChapterVo>();
        Document doc = SpiderUtil.getDocment(queryUrl);
        logger.info("[XmwbService] getChapterLists doc={} "  , doc);
        //通过class 获取列表下的所有
        Elements postItems = doc.getElementsByClass("dzb-enter-mulu-wrap-nav");
        if(postItems != null){
            for(Element e : postItems){
                XmwbChapterVo chapterVo = new XmwbChapterVo();
                String desc = e.select("a").text();
                String url = e.select("a").attr("href");
                chapterVo.setDesc(desc);
                chapterVo.setChapterUrl(url);
                chapterVo.setObjectUrl(XmwbConstant.URL_CHAPTER_PRE+chapterVo.getChapterUrl());
                retList.add(chapterVo);
            }
        }

        return retList;
    }

    @Override
    public boolean collectChapter(String chapterUrl ,String chapterDesc) {
        if(StringUtils.isBlank(chapterUrl)) {
            return false;
        }

        List<XmwbArticleVo> list =  this.queryXmwbArticleListByChapter(chapterUrl,chapterDesc);
        if(list != null){
            for(XmwbArticleVo vo : list){
               this.collectArticle(vo.getArticleUrl() ,vo.getChapterDesc());
            }
        }

        return true;
    }

    @Override
    public boolean collectArticle(String articleUrl ,String chapterDesc) {
        if(StringUtils.isBlank(articleUrl)) {
            return false;
        }

        XmwbArticleVo objectVo = this.queryXmwbArticleDetail(articleUrl , chapterDesc);
        redisService.hmSet(Constant.CACHE_XMWB_ART_HASH , objectVo.getArticleUrl(),objectVo);
        redisService.expire(Constant.CACHE_XMWB_ART_HASH );

        redisService.zAdd(Constant.CACHE_XMWB_PRINTARTLIST_ZSET , objectVo.getArticleUrl() ,objectVo.getSorce());
        redisService.expire(Constant.CACHE_XMWB_PRINTARTLIST_ZSET );

        return true;
    }

    @Override
    public List<XmwbArticleVo> queryXmwbCollectList() {
        List<XmwbArticleVo> list  = new ArrayList<XmwbArticleVo>();
        long count = redisService.zCard(Constant.CACHE_XMWB_PRINTARTLIST_ZSET );
        logger.info("[XmwbService] queryXmwbCollectList count={} " , count);
        int step = 20;
        if(count > 0){
            int start = 0;
            int end = step -1;
            boolean flag = true;
            while(flag){
                logger.info("[XmwbService] queryXmwbCollectList begin:start={} ,end={} , flag={}" , start , end, flag);
                Set<Object> set = redisService.zRange(Constant.CACHE_XMWB_PRINTARTLIST_ZSET , start ,end);
                Set<String> artSet = new LinkedHashSet<String>();
                if(set != null && set.size() > 0) {
                    for(Object o : set) {
                        artSet.add((String) o);
                    }
                    list.addAll(redisService.hmGetALL(Constant.CACHE_XMWB_ART_HASH , new ArrayList(artSet)));
                    start = start + step;
                    end = end + step;
                }else{
                    flag = false;
                }
                logger.info("[XmwbService] queryXmwbCollectList end:start={} ,end={} , flag={}" , start , end, flag);
            }
        }
        return list;
    }

    @Override
    public boolean delAllPrintList() {
        return redisService.del(Constant.CACHE_XMWB_PRINTARTLIST_ZSET);
    }

    @Override
    public long delFromPrintList(String artId) {
        String[] artids = new String[1];
        artids[0] = artId;
        return redisService.zDel(Constant.CACHE_XMWB_PRINTARTLIST_ZSET, artids);
    }


    @Override
    public List<XmwbArticleVo> queryXmwbArticleListByChapter(String chapterUrl , String chapterDesc){
        List<XmwbArticleVo> list  = new ArrayList<XmwbArticleVo>();
        if(list.isEmpty()){
            String url = XmwbConstant.URL_CHAPTER_PRE + chapterUrl;
            Document doc = SpiderUtil.getDocment(url);
            logger.info("[XmwbService] queryXmwbChapterList url={} ,doc={} " , url, doc);

            Element map = doc.getElementById("PagePicMap");
            Elements articleList =map.getElementsByTag("area");
            if(articleList != null){
                for(Element e : articleList){
                    XmwbArticleVo vo = new XmwbArticleVo();
                    vo.setDesc(e.attr("title"));
                    vo.setArticleUrl(e.attr("href"));
                    vo.setChapterDesc(chapterDesc);
                    list.add(vo);
                }
            }
        }

        return list;
    }

    @Override
    public XmwbArticleVo queryXmwbArticleDetail(String articleUrl , String chapterDesc) {
        String url = XmwbConstant.URL_CHAPTER_PRE + articleUrl;
        Document doc = SpiderUtil.getDocment(url);
        logger.info("[XmwbService] queryXmwbArticleDetail url={} ,doc={} " , url, doc);

        XmwbArticleVo vo = new XmwbArticleVo();
        vo.setArticleUrl(articleUrl);
        vo.setChapterDesc(chapterDesc);
        Elements articles = doc.getElementsByClass("dzb-enter-desc-box dzb-enter-heng-desc-box");
        if(articles != null){
            Element article = articles.first();

            Elements es = article.getElementsByClass("dzb-author-box");
            if(es != null){
                String author = es.first().text().replaceAll(" ","");
                vo.setArtAhthor(author);
            }


            Elements es2 = article.getElementsByClass("dzb-desc-box");
            if(es2 != null){
                String content = es2.first().text();
                vo.setArtContent(delAhthorFromContent(vo.getArtAhthor(),content));
            }


            Elements es3 = article.getElementsByClass("dzb-title-box");
            if(es3 != null){
                String title = es3.first().text();
                vo.setDesc(title);
            }
        }

        return vo;
    }

    private String delAhthorFromContent(String author , String content){
        if(content.startsWith(author)){
            return content.substring(author.length());
        }else{
            char[] chgAuthor = author.toCharArray();
            StringBuffer sb = new StringBuffer();
            for(char c : chgAuthor){
                sb.append(c);
                sb.append(" ");
            }
            if(content.startsWith(sb.toString())){
                return content.substring(sb.toString().length());
            }
        }

        return content;
    }

    @Override
    public void startTaskForPrintWord(String path ) {
        TaskThreadPool.getInstance().exec(new XmwbPrintWordThread(path ));
    }

}
