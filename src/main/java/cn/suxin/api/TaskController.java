package cn.suxin.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.suxin.constant.Constant;
import cn.suxin.constant.IniBean;
import cn.suxin.model.ArticleInfo;
import cn.suxin.model.ArticleInfoVo;
import cn.suxin.model.TaskModel;
import cn.suxin.sevice.JwbService;
import cn.suxin.util.DateUtil;
import cn.suxin.util.JsonUtils;

@RestController
@RequestMapping("/query")
public class TaskController {

    private static Logger log = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    JwbService jwbService;
    
    @RequestMapping(value = "/startTask")
    public Map<String, Object> startTask(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String taskDesc = request.getParameter("taskDesc");
        
        if(StringUtils.isEmpty(startDateStr) || 
                        StringUtils.isEmpty(endDateStr) ) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                if(StringUtils.isEmpty(taskDesc)) {
                    taskDesc = "任务" + IniBean.generId();
                }
                
                Date startDate = DateUtil.formatString(startDateStr, DateUtil.DEFAULT_DATE_FORMAT);
                Date endDate = DateUtil.formatString(endDateStr, DateUtil.DEFAULT_DATE_FORMAT);
                
                TaskModel model = jwbService.taskStart(taskDesc, startDate, endDate);
                
                Thread.sleep(3000);
                
                ret.put(Constant.RET_DETAIL, model);
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
            }
        }
        log.info("[startTask] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    @RequestMapping(value = "/queryTask")
    public Map<String, Object> queryTask(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String taskId = request.getParameter("taskId");
        
        if(StringUtils.isEmpty(taskId)) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                long taskIdLong  = Long.parseLong(taskId);
                
                TaskModel model = jwbService.queryTask(taskIdLong);
                
                ret.put(Constant.RET_DETAIL, model);
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
            }
        }
        log.info("[queryTask] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    
    @RequestMapping(value = "/delTask")
    public Map<String, Object> delTask(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String taskId = request.getParameter("taskId");
        
        if(StringUtils.isEmpty(taskId)) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                long taskIdLong  = Long.parseLong(taskId);
                
                jwbService.delTask(taskIdLong);
                
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
            }
        }
        log.info("[delTask] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    @RequestMapping(value = "/queryAllArticleListByDate")
    public Map<String, Object> queryAllArticleListByDate(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        if(StringUtils.isEmpty(startDateStr) || 
                        StringUtils.isEmpty(endDateStr)) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                Date startDate = DateUtil.formatString(startDateStr, DateUtil.DEFAULT_DATE_FORMAT);
                Date endDate = DateUtil.formatString(endDateStr, DateUtil.DEFAULT_DATE_FORMAT);
                
                List<ArticleInfoVo> list = jwbService.queryArticleList(startDate, endDate);
                
                ret.put(Constant.RET_DETAIL, list);
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
                e.printStackTrace();
            }
        }
        log.info("[queryAllArticleListByDate] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    
    @RequestMapping(value = "/queryArticleListByArtIds")
    public Map<String, Object> queryArticleListByArtIds(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String artIds = request.getParameter("artIds");
        
        if(StringUtils.isEmpty(artIds)) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                String[] artIdList = artIds.split(",");
                
                List<ArticleInfo> list = jwbService.queryArticleListByIds(artIdList);
                
                ret.put(Constant.RET_DETAIL, list);
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
                e.printStackTrace();
            }
        }
        log.info("[queryArticleListByArtIds] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    
    @RequestMapping(value = "/startSpiderActicleDetailByArtIds")
    public Map<String, Object> startSpiderActicleDetailByArtIds(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String artIds = request.getParameter("artIds");
        
        if(StringUtils.isEmpty(artIds)) {
            ret.put(Constant.RET_CODE, 401);
            ret.put(Constant.RET_DESC, "参数有误！");
        }else {
            try {
                
                String[] artIdList = artIds.split(",");
                
                TaskModel model = jwbService.startSpiderActicleDetailByArtIds(artIdList);
                
                ret.put(Constant.RET_DETAIL, model);
                ret.put(Constant.RET_CODE, 200);
                ret.put(Constant.RET_DESC, "已经提交！");
            } catch (Exception e) {
                ret.put(Constant.RET_CODE, 500);
                ret.put(Constant.RET_DESC, "提交失败！");
                e.printStackTrace();
            }
        }
        log.info("[startSpiderActicleDetailByArtIds] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
    
    
    @RequestMapping(value = "/startTaskForPrintWord")
    public Map<String, Object> startTaskForPrintWord(ServletRequest request) {

        Map<String, Object> ret = new HashMap<>();
        
        String artIds = request.getParameter("artIds");
        String path = request.getParameter("path");
        
     try {
            
            if(StringUtils.isEmpty(path)) {
                path = "E:\\jwb_" + DateUtil.formatDate(new Date(), DateUtil.FMT_YYYYMMDDHHMMSS)+".doc";
            }
            
            String[] artIdList = null;
            
            if(StringUtils.isEmpty(artIds)) {
            	Set<String> setList = jwbService.getPrintList();
            	if(setList != null && setList.size() >0) {
            		artIdList = setList.toArray(new String[setList.size()]);
            	}
            }else {
            	artIdList = artIds.split(",");
            }
            
            
            TaskModel model = jwbService.startTaskForPrintWord(artIdList,path);
            log.info("[startTaskForPrintWord] model {} ", JsonUtils.toJson(model));
            ret.put(Constant.RET_DETAIL, path);
            ret.put(Constant.RET_CODE, 200);
            ret.put(Constant.RET_DESC, "已经提交！");
        } catch (Exception e) {
            ret.put(Constant.RET_CODE, 500);
            ret.put(Constant.RET_DESC, "提交失败！");
            e.printStackTrace();
        }
        
        log.info("[startTaskForPrintWord] ret {} ", JsonUtils.toJson(ret));
        return ret;
    }
}
