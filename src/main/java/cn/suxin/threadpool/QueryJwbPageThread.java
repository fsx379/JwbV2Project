package cn.suxin.threadpool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.suxin.constant.Constant;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.FKInfo;
import cn.suxin.model.PageInfo;
import cn.suxin.model.TaskModel;
import cn.suxin.sevice.JwbSpiderUtil;
import cn.suxin.util.DateUtil;
import cn.suxin.util.JsonUtils;

public class QueryJwbPageThread extends TaskThread{
    
    public QueryJwbPageThread(TaskModel task) {
        super(task);
    }
    
    @Override
    public void runTaskJob() throws InterruptedException {
        Date runingDate = task.getQueryStartDate();
        List<PageInfo> pageInfoList = new ArrayList<>();
        while(runingDate.compareTo(task.getQueryEndDate()) <=0 ) {
            
            this.runTask(pageInfoList , runingDate);
           
            runingDate = DateUtil.getIntervalBeginOfDay(runingDate, -1);
            Thread.sleep(500);
        }
        
        //数据保存
        this.dataSave(pageInfoList);
        
        log.info("[QueryJwbThread] task : " +  JsonUtils.toJson(task));
        log.info("[QueryJwbThread] detail : " +  JsonUtils.toJson(pageInfoList));
        
    }
    
    private void runTask(List<PageInfo> pageInfoList , Date runingDate) {
        String runingDateStr = DateUtil.formatDate(runingDate);
        PageInfo info  = new PageInfo(runingDateStr);
        pageInfoList.add(info);
        
        String firstP = runingDateStr.substring(0, 7);
        String secondP = runingDateStr.substring(8, 10);
        String thirdP = "node_1.htm";
        String url = JwbSpiderUtil.getURL(firstP, secondP, thirdP);
        
        log.info("[QueryJwbThread] url: " +  url);
        //所有副刊列表
        JwbSpiderUtil.queryPageInfo(url,info, firstP, secondP);
        
        //查询文章列表(仅标题)
        JwbSpiderUtil.queryArticleList(info, firstP, secondP);
        
    }
    
    
    /**
     * 数据存储
     * @param info
     */
    private void dataSave( List<PageInfo>  infoList) {
        if(infoList != null && infoList.size() >0 ) {
            List<String> articleIdList = new ArrayList<>();
            Map<String, Object> artMap = new HashMap<>();
            
            for(PageInfo info : infoList) {
                
                articleIdList.clear();
                artMap.clear();
                
                List<FKInfo> fkList = info.getList();
                if(fkList != null && fkList.size() > 0) {
                    for(FKInfo fk : fkList) {
                        List<ArticleInfo> artList = fk.getArtList();
                        if(artList != null && artList.size() >0) {
                            for(ArticleInfo art : artList) {
                                articleIdList.add(art.getArtId());
                                artMap.put(art.getArtId(), art);
                            }
                            
                        }
                    }
                }
                
                long size = redisService.lSize(Constant.CACHE_PAGE_LIST_PRE+info.getPageDate());
                if(size > 0 ) {
                    List<String> tmpList = redisService.lRange(Constant.CACHE_PAGE_LIST_PRE+info.getPageDate(), 0, -1);
                    redisService.remove(Constant.CACHE_PAGE_LIST_PRE+info.getPageDate());
                    redisService.hmDelete(Constant.CACHE_ARCTICLE_HASH, tmpList.toArray(new String[tmpList.size()]));
                }
                
                if(articleIdList != null && articleIdList.size() > 0) {
                    redisService.lPushAll(Constant.CACHE_PAGE_LIST_PRE+info.getPageDate(), articleIdList);
                    redisService.hmSetAll(Constant.CACHE_ARCTICLE_HASH, artMap);
                }
                
            }
        }
    }

        

}
