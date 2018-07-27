package cn.suxin.model;

import java.io.Serializable;
import java.util.Date;
import cn.suxin.constant.Constant;
import cn.suxin.util.DateUtil;

public class TaskModel implements Serializable  {
    
    private static final long serialVersionUID = 805929885311289018L;
    
    long taskId;            //任务ID
    long taskCreaTime;      
    
    long taskRunStartTime ; //任务开始执行时间
    long taskRunEndTime;    //任务执行结束时间
    long taskUpdateTime;
    int taskStatus;        //任务开始执行时间  0 准备开始  1 进行中  9 结束   -1 异常
    
    
    int taskType;   //1扒列表 2扒具体内容 3 打印
    
    /**
     * 任务1 数据
     */
    String taskDesc;        //任务描述
    Date queryStartDate;  //需要查询的开始时间
    Date queryEndDate;    //需要查询的结束时间
    String queryStartDateStr;
    String queryEndDateStr;
    
    /**
     * 任务2数据
     */
    String[] arcticleIds;
    
    String path;
    
    public TaskModel() {
        this.taskId = System.currentTimeMillis();
        this.taskStatus = Constant.TASK_INI;
        this.taskCreaTime = System.currentTimeMillis();
        this.taskUpdateTime = this.taskCreaTime;
    }
    
    public TaskModel(String taskDesc ,Date queryStartDate ,Date queryEndDate) {
        this.taskType = Constant.TASK_TYPE_PAGE;
        this.taskId = System.currentTimeMillis();
        this.taskStatus = Constant.TASK_INI;
        this.taskCreaTime = System.currentTimeMillis();
        this.taskUpdateTime = this.taskCreaTime;
        
        this.taskDesc = taskDesc;
        this.queryStartDate = queryStartDate;
        this.queryEndDate = queryEndDate;
        
        this.queryStartDateStr = DateUtil.formatDate(queryStartDate, DateUtil.DEFAULT_DATE_FORMAT);
        this.queryEndDateStr = DateUtil.formatDate(queryEndDate, DateUtil.DEFAULT_DATE_FORMAT);
    }
    
    public TaskModel(String[] articleIds , String taskDesc ,int taskType) {
        this.taskType = taskType;
        this.taskId = System.currentTimeMillis();
        this.taskStatus = Constant.TASK_INI;
        this.taskCreaTime = System.currentTimeMillis();
        this.taskUpdateTime = this.taskCreaTime;
        
        this.taskDesc = taskDesc;
        this.arcticleIds = articleIds;
        
    }
    
    public long getTaskCreaTime() {
        return taskCreaTime;
    }

    public void setTaskCreaTime(long taskCreaTime) {
        this.taskCreaTime = taskCreaTime;
    }

  

    public long getTaskId() {
        return taskId;
    }
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    public String getTaskDesc() {
        return taskDesc;
    }
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
    public long getTaskRunStartTime() {
        return taskRunStartTime;
    }
    public void setTaskRunStartTime(long taskRunStartTime) {
        this.taskRunStartTime = taskRunStartTime;
    }
    public long getTaskRunEndTime() {
        return taskRunEndTime;
    }
    public void setTaskRunEndTime(long taskRunEndTime) {
        this.taskRunEndTime = taskRunEndTime;
    }
    public int getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
    public Date getQueryStartDate() {
        return queryStartDate;
    }
    public void setQueryStartDate(Date queryStartDate) {
        this.queryStartDate = queryStartDate;
    }
    public Date getQueryEndDate() {
        return queryEndDate;
    }
    public void setQueryEndDate(Date queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public String getQueryStartDateStr() {
        return queryStartDateStr;
    }

    public void setQueryStartDateStr(String queryStartDateStr) {
        this.queryStartDateStr = queryStartDateStr;
    }

    public String getQueryEndDateStr() {
        return queryEndDateStr;
    }

    public void setQueryEndDateStr(String queryEndDateStr) {
        this.queryEndDateStr = queryEndDateStr;
    }

    public long getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(long taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String[] getArcticleIds() {
        return arcticleIds;
    }

    public void setArcticleIds(String[] arcticleIds) {
        this.arcticleIds = arcticleIds;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    

}
