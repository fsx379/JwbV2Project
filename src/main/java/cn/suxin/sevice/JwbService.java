package cn.suxin.sevice;

import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.suxin.model.ArticleInfo;
import cn.suxin.model.ArticleInfoVo;
import cn.suxin.model.TaskModel;

public interface JwbService {
    
    TaskModel taskStart(String taskDesc , Date startTime,Date endTime);

    TaskModel startSpiderActicleDetailByArtIds(String[] artIdList);
    
    TaskModel startTaskForPrintWord(String[] artIdList,String path);
    
    TaskModel queryTask(long taskId);
    
    void delTask(Long taskId);
    
    List<ArticleInfoVo>  queryArticleList( Date startTime, Date endTime);
    
    List<ArticleInfo>  queryArticleListByIds(String[] artIds);
    
    boolean addPrintList(String artId) ;
    
    long delFromPrintList(String artId);
    
    boolean delAllPrintList();
    
    Set<String> getPrintList();
    
    List<ArticleInfo> getPrintArticleList();
    
    ArticleInfo queryArticleDetail(String artId);

}
