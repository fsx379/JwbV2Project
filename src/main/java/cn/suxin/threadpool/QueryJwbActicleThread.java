package cn.suxin.threadpool;

import cn.suxin.constant.Constant;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.TaskModel;
import cn.suxin.sevice.JwbSpiderUtil;
import cn.suxin.util.JsonUtils;

public class QueryJwbActicleThread  extends TaskThread  {


    public QueryJwbActicleThread(TaskModel task) {
        super(task);
     }
    

    @Override
    public void runTaskJob() throws InterruptedException  {
        String[] articleIds = task.getArcticleIds();
        if(articleIds != null && articleIds.length >0){
            for(String artId : articleIds) {
                
                ArticleInfo artInfo = (ArticleInfo) redisService.hmGet(Constant.CACHE_ARCTICLE_HASH, artId);
                
                JwbSpiderUtil.queryArticleDetal(artInfo);
                redisService.hmSet(Constant.CACHE_ARCTICLE_HASH, artInfo.getArtId(), artInfo);
                Thread.sleep(400);
            }
        }
        
        log.info("[QueryJwbActicleThread] task : " +  JsonUtils.toJson(task));
    }



}
