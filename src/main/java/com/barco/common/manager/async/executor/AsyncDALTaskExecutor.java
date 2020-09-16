package com.barco.common.manager.async.executor;

import com.barco.common.utility.ExceptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


public class AsyncDALTaskExecutor {

    public static Logger logger = LogManager.getLogger(AsyncDALTaskExecutor.class);

    private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static ThreadPoolExecutor threadPool;

    public static void addTask(Runnable task) {
        try {
            logger.debug("Submitting Task of type : " + task.getClass().getCanonicalName());
            threadPool.submit(task);
        } catch (RejectedExecutionException ex) {
            logger.error("Failed to submit Task in queue " + ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    public AsyncDALTaskExecutor(Integer minThreads, Integer maxThreads, Integer threadLifeInMins) {
        logger.info(">============AsyncDALTaskExecutor Start Successful============<");
        threadPool = new ThreadPoolExecutor(minThreads, maxThreads, threadLifeInMins, TimeUnit.MINUTES, queue);
        threadPool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
                logger.error("Task Rejected : " + task.getClass().getCanonicalName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    logger.error("DAL Task Interrupted " + ExceptionUtil.getRootCauseMessage(ex));
                }
                // if task reject then add same take for execution again
                executor.execute(task);
            }
        });
        // scheduler use to check how man thread are active and other pool size detail
        (new Timer())
            .schedule(new TimerTask() {
                @Override public void run() {
                logger.info("AsyncDAL Active No Threads: " + threadPool.getActiveCount() +
                    " Core no of Threads: " + threadPool.getCorePoolSize() +
                    " Current no of threads: " + threadPool.getPoolSize() +
                    " Current Queue Size: " + queue.size() +
                    " Max allowed Threads: "+threadPool.getMaximumPoolSize());
                }
            }, 5 * 60 * 1000, 60000);
        logger.info(">============AsyncDALTaskExecutor End Successful============<");
    }

}
