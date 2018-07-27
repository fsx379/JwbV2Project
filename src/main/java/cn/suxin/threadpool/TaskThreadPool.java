package cn.suxin.threadpool;


import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by panchao on 16/7/1.
 */
public class TaskThreadPool {
    
    private static Logger log = LoggerFactory.getLogger(TaskThreadPool.class);
    
    private static TaskThreadPool taskThreadPool;
    
    private static ThreadPoolExecutor exec;
    
    private static volatile Object lock = new Object();

    private TaskThreadPool(){
        int coreNum = Runtime.getRuntime().availableProcessors() * 2;
        exec = new ThreadPoolExecutor(coreNum,coreNum,5 * 60, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(10000),
                new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error("notification persistence thread pool is full");
            }
        });
    }

    public static TaskThreadPool getInstance(){
        if(taskThreadPool == null){
            synchronized (lock){
                if(taskThreadPool == null){
                    taskThreadPool = new TaskThreadPool();
                }
            }
        }
        return taskThreadPool;
    }

    public int getQueueSize(){
        return exec.getQueue().size();
    }
    public void exec(Runnable command){
        exec.execute(command);
    }
}
