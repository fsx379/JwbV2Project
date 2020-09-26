package cn.suxin.sevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.suxin.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.suxin.constant.Constant;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.ArticleInfoVo;
import cn.suxin.model.TaskModel;
import cn.suxin.redis.RedisService;
import cn.suxin.threadpool.PrintWordThread;
import cn.suxin.threadpool.QueryJwbActicleThread;
import cn.suxin.threadpool.QueryJwbPageThread;
import cn.suxin.threadpool.TaskThreadPool;
import cn.suxin.util.DateUtil;

@Service
public class JwbServiceImp implements JwbService {


    @Autowired
    RedisService redisService;
    
    @Override
    public TaskModel taskStart(String taskDesc , Date startTime, Date endTime) {
        TaskModel task = new TaskModel(taskDesc, startTime, endTime);
        
        TaskThreadPool.getInstance().exec(new QueryJwbPageThread(task));
        
        redisService.hmSet(Constant.CACHE_TASK_LIST, task.getTaskId(), task);
        return task;
    }

    
    @Override
    public TaskModel startSpiderActicleDetailByArtIds(String[] artIdList) {
        TaskModel task = new TaskModel(artIdList, "抓文章内容" ,Constant.TASK_TYPE_ACTICLE);
        
        TaskThreadPool.getInstance().exec(new QueryJwbActicleThread(task));
        redisService.hmSet(Constant.CACHE_TASK_LIST, task.getTaskId(), task);
        return task;
    }
    
    
    @Override
    public TaskModel startTaskForPrintWord(String[] artIdList ,String path) {
        TaskModel task = new TaskModel(artIdList, "打印到word中",Constant.TASK_TYPE_PRINT);
        task.setPath(path);
        TaskThreadPool.getInstance().exec(new PrintWordThread(task));
        redisService.hmSet(Constant.CACHE_TASK_LIST, task.getTaskId(), task);
        return task;
    }

    
    @Override
    public TaskModel queryTask(long taskId) {
        // TODO Auto-generated method stub
        return (TaskModel)redisService.hmGet(Constant.CACHE_TASK_LIST, taskId);
    }
    
    @Override
    public void delTask(Long taskId) {
        Long[] taskIs = new  Long[1];
        taskIs[0] = taskId;
        redisService.hmDelete(Constant.CACHE_TASK_LIST, taskIs);
    }


    @Override
    public List<ArticleInfoVo> queryArticleList(Date startTime, Date endTime) {
        
        List<ArticleInfoVo> list = new ArrayList<>();
        Date queryDate = startTime;
        while(queryDate.compareTo(endTime) <= 0){
            String queryDateStr = DateUtil.formatDate(queryDate);
            ArticleInfoVo vo = new ArticleInfoVo(queryDateStr);
            list.add(vo);
            
            String key = Constant.CACHE_PAGE_LIST_PRE + queryDateStr;
            long size = redisService.lSize(key);
            if(size > 0) {
                List<String> tmplist = redisService.lRange(key, 0, -1);
                List<ArticleInfo> listAricleInfo = redisService.hmGetALL(Constant.CACHE_ARCTICLE_HASH, tmplist);
                vo.addList(listAricleInfo);
            }
            queryDate = DateUtil.getIntervalDate(queryDate, 1);
        }
        
        return list;
    }

    @Override
    public List<ArticleInfo> queryArticleListByIds(String[] artIds) {
        List<ArticleInfo> listAricleInfo =  null;
        if(artIds != null && artIds.length > 0) {
            List<String> artList = Arrays.asList(artIds);
            listAricleInfo = redisService.hmGetALL(Constant.CACHE_ARCTICLE_HASH, artList);
        }
        return listAricleInfo;
    }


	@Override
	public boolean addPrintList(String artId) {
		
		if(StringUtils.isBlank(artId)) {
			return false;
		}
		
		ArticleInfo articleInfo = (ArticleInfo) redisService.hmGet(Constant.CACHE_ARCTICLE_HASH, artId);
		String log = "getArtcle: " + artId + " " + JsonUtils.toJson(articleInfo);
		System.out.println("[addPrintList] " +log);
		if(articleInfo == null) {
			return false;
		}else {
			if(StringUtils.isBlank(articleInfo.getArtAhthor())
					|| StringUtils.isBlank(articleInfo.getArtContent())) {
				String[] artList = new String[1];
				artList[0] = artId;
				this.startSpiderActicleDetailByArtIds(artList);
			}
			long scoure = DateUtil.formatString(articleInfo.getArtDate(), "yyyy-MM-dd").getTime();
			redisService.zAdd(Constant.CACHE_PRINTARTLIST_ZSET, articleInfo.getArtId(),scoure );
		}
			
		return true;
	}

	@Override
	public long delFromPrintList(String artId) {
		String[] artids = new String[1];
		artids[0] = artId;
		return redisService.zDel(Constant.CACHE_PRINTARTLIST_ZSET, artids);
	}
	
	@Override
	public boolean delAllPrintList() {
		// TODO Auto-generated method stub
		return redisService.del(Constant.CACHE_PRINTARTLIST_ZSET);
	}
	
	@Override
	public Set<String> getPrintList() {
		long now = System.currentTimeMillis();
		Set<String> artSet = new LinkedHashSet<String>();
		Set<Object>  set = redisService.zrangeByScore(Constant.CACHE_PRINTARTLIST_ZSET, 0, now);
		if(set != null && set.size() > 0) {
			for(Object o : set) {
				artSet.add((String) o);
			}
		}
		
		return artSet;
	}


	@Override
	public List<ArticleInfo> getPrintArticleList() {
		List<ArticleInfo> listAricleInfo =  null;
		Set<String> printIdsSet = this.getPrintList();
		if(printIdsSet != null && printIdsSet.size() > 0) {
			List<String> artList = new ArrayList<String>();
			artList.addAll(printIdsSet);
			listAricleInfo = (List<ArticleInfo>) redisService.hmGetALL(Constant.CACHE_ARCTICLE_HASH, artList);
		}
		return listAricleInfo;
	}


	@Override
	public ArticleInfo queryArticleDetail(String artId) {
		return (ArticleInfo) redisService.hmGet(Constant.CACHE_ARCTICLE_HASH, artId);
	}








}
