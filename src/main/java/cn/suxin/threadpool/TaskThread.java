package cn.suxin.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.suxin.constant.Constant;
import cn.suxin.model.TaskModel;
import cn.suxin.redis.RedisService;
import cn.suxin.util.JsonUtils;
import cn.suxin.util.SpringContextHolder;

public abstract class TaskThread implements Runnable{
    
    protected static Logger log = LoggerFactory.getLogger(QueryJwbActicleThread.class);

    TaskModel task;

    RedisService redisService;

    public TaskThread(TaskModel task) {
        this.task = task;
        this.redisService = SpringContextHolder.getBean("redisService");
    }
    
    public abstract void runTaskJob() throws Exception;

    @Override
    public void run() {
        try {
        	log.info("[TaskThread] " + JsonUtils.toJson(task));
            this.updateTaskStatus(Constant.TASK_RUNNING);
            this.runTaskJob();
            this.updateTaskStatus(Constant.TASK_END_SUCCESS);
            log.info("[TaskThread] " + JsonUtils.toJson(task));
        } catch (Exception e) {
            this.updateTaskStatus(Constant.TASK_END_FAIL);
            log.error("[TaskThread] error! ", e);
        }
    }
    
    protected void updateTaskStatus(int status) {
        this.task.setTaskStatus(status);

        if (status == Constant.TASK_END_SUCCESS || status == Constant.TASK_END_FAIL) {
            this.task.setTaskRunEndTime(System.currentTimeMillis());
            this.task.setTaskUpdateTime(System.currentTimeMillis());
        } else if (status == Constant.TASK_RUNNING) {
            this.task.setTaskRunStartTime(System.currentTimeMillis());
            this.task.setTaskUpdateTime(System.currentTimeMillis());
        }

        redisService.hmSet(Constant.CACHE_TASK_LIST, this.task.getTaskId(), this.task);
    }
}
