package com.barco.common.manager.async.executor;

import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.*;

/**
 * @author Nabeel Ahmed
 */
public class AsyncDALTaskExecutor {

    // Use correct class for logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncDALTaskExecutor.class);

    private static final int MAX_REJECT_ATTEMPTS = 3;
    private static ScheduledExecutorService scheduler;
    private static ThreadPoolTaskExecutor threadPoolTask;

    /**
     * If max threads reach the limit the new thread reject state so its depend on the requirement
     * either rejected thread add again into the thread or else save in the db with the as inQueue status
     * @param corePoolSize
     * @param maxPoolSize
     * @param queueCapacity
     * @param keepAlive
     * */
    public AsyncDALTaskExecutor(Integer corePoolSize, Integer maxPoolSize, Integer queueCapacity, Integer keepAlive) {
        LOGGER.info(">============AsyncDALTaskExecutor Start Successful============<");
        // Avoid re-initializing static executor if already started
        synchronized (AsyncDALTaskExecutor.class) {
            if (!BarcoUtil.isNull(this.threadPoolTask) && !BarcoUtil.isNull(this.threadPoolTask.getThreadPoolExecutor())
                && !this.threadPoolTask.getThreadPoolExecutor().isShutdown()) {
                LOGGER.info("AsyncDALTaskExecutor already initialized, skipping re-init");
                return;
            }
            this.threadPoolTask = new ThreadPoolTaskExecutor();
            this.threadPoolTask.setCorePoolSize(corePoolSize);
            this.threadPoolTask.setMaxPoolSize(maxPoolSize);
            this.threadPoolTask.setQueueCapacity(queueCapacity);
            this.threadPoolTask.setKeepAliveSeconds(keepAlive);
            this.threadPoolTask.setAllowCoreThreadTimeOut(false);
            this.threadPoolTask.setThreadNamePrefix("task-");
            // Ensure orderly shutdown waits for tasks
            this.threadPoolTask.setWaitForTasksToCompleteOnShutdown(true);
            this.threadPoolTask.setAwaitTerminationSeconds(30);
            // RejectedExecutionHandler that will retry PrioritizedTask up to MAX_REJECT_ATTEMPTS
            this.threadPoolTask.setRejectedExecutionHandler((Runnable task, ThreadPoolExecutor executor) -> {
                try {
                    if (task instanceof PrioritizedTask) {
                        PrioritizedTask pTask = (PrioritizedTask) task;
                        int attempts = pTask.getRejectAttempts().incrementAndGet();
                        if (attempts <= MAX_REJECT_ATTEMPTS) {
                            LOGGER.warn("Task Rejected, retrying attempt {}/{} for task id: {}, type: {}", attempts, MAX_REJECT_ATTEMPTS, pTask.getTaskId(),
                                !BarcoUtil.isNull(pTask.getTask()) ? pTask.getTask().getClass().getCanonicalName() :
                                     pTask.getClass().getCanonicalName());
                            try {
                                // brief backoff before retry
                                Thread.sleep(1000L);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                                LOGGER.error("DAL Task Interrupted during backoff :- {}.", ExceptionUtil.getRootCauseMessage(ie));
                                return;
                            }
                            try {
                                executor.execute(task);
                            } catch (RejectedExecutionException rex) {
                                LOGGER.error("Retry submission failed for attempt {} for task id: {} type: {} :- {}", attempts, pTask.getTaskId(),
                                    !BarcoUtil.isNull(pTask.getTask()) ? pTask.getTask().getClass().getCanonicalName() :
                                         pTask.getClass().getCanonicalName(), ExceptionUtil.getRootCauseMessage(rex));
                            }
                        } else {
                            LOGGER.error("Max reject attempts ({}) reached for task id: {} type: {}. Dropping task.", MAX_REJECT_ATTEMPTS, pTask.getTaskId(),
                                !BarcoUtil.isNull(pTask.getTask()) ? pTask.getTask().getClass().getCanonicalName() :
                                    pTask.getClass().getCanonicalName());
                        }
                    } else {
                        LOGGER.error("Task Rejected :- {}.", task.getClass().getCanonicalName());
                        try {
                            // wait for 1 second if task is rejected
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            LOGGER.error("DAL Task Interrupted  :- {}.", ExceptionUtil.getRootCauseMessage(ex));
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.error("Unexpected error in RejectedExecutionHandler :- {}.", ExceptionUtil.getRootCauseMessage(ex));
                }
            });
            // initialize after handler is set
            this.threadPoolTask.initialize();
            // Named daemon thread for scheduler so it won't block JVM shutdown
            this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "async-dal-scheduler");
                thread.setDaemon(true);
                return thread;
            });
        }
        this.scheduler.scheduleAtFixedRate(() -> {
            LOGGER.info("Active: {}, PoolSize: {}, Core: {}, Max: {}, Queue: {}",
                this.threadPoolTask.getActiveCount(),
                this.threadPoolTask.getPoolSize(),
                this.threadPoolTask.getCorePoolSize(),
                this.threadPoolTask.getThreadPoolExecutor().getMaximumPoolSize(),
                this.threadPoolTask.getThreadPoolExecutor().getQueue().size());
        }, 5 * 60, 60, TimeUnit.SECONDS);
        LOGGER.info(">============AsyncDALTaskExecutor End Successful============<");
    }

    /**
     * This method use to add the task in thread pool
     * @param task
     * */
    public static void addTask(PrioritizedTask task) {
        if (BarcoUtil.isNull(task)) {
            LOGGER.warn("addTask() called with null task");
            throw new IllegalArgumentException("task must not be null");
        }
        if (BarcoUtil.isNull(threadPoolTask) || BarcoUtil.isNull(threadPoolTask.getThreadPoolExecutor())
            || threadPoolTask.getThreadPoolExecutor().isShutdown()) {
            LOGGER.error("ThreadPoolTaskExecutor is not initialized or already shutdown");
            throw new IllegalStateException("Executor not initialized");
        }
        try {
            LOGGER.info("Submitting Task id: {}, type: {}.", task.getTaskId(),
                BarcoUtil.isNull(task.getTask()) ? task.getClass().getCanonicalName() : task.getTask().getClass().getCanonicalName());
            threadPoolTask.execute(task);
        } catch (RejectedExecutionException ex) {
            LOGGER.error("Failed to submit Task in queue :- {}.", ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    /**
     * Method to shut down the thread pool and scheduler gracefully. It will wait for existing tasks to complete before shutting down.
     * **/
    public void shutdown() {
        LOGGER.info("Shutting down AsyncDALTaskExecutor...");
        if (!BarcoUtil.isNull(this.scheduler)) {
            this.scheduler.shutdown();
            try {
                if (!this.scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                    this.scheduler.shutdownNow();
                }
            } catch (InterruptedException ex) {
                this.scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        if (!BarcoUtil.isNull(this.threadPoolTask)) {
            // Initiate graceful shutdown and wait for tasks to complete
            this.threadPoolTask.shutdown();
            try {
                if (!BarcoUtil.isNull(this.threadPoolTask.getThreadPoolExecutor()) &&
                    !this.threadPoolTask.getThreadPoolExecutor().awaitTermination(30, TimeUnit.SECONDS)) {
                    this.threadPoolTask.getThreadPoolExecutor().shutdownNow();
                }
            } catch (InterruptedException ex) {
                if (!BarcoUtil.isNull(this.threadPoolTask.getThreadPoolExecutor())) {
                    this.threadPoolTask.getThreadPoolExecutor().shutdownNow();
                }
                Thread.currentThread().interrupt();
            } finally {
                this.threadPoolTask = null;
            }
        }
        LOGGER.info("AsyncDALTaskExecutor shutdown complete.");
    }

}