package com.barco.common.manager.async.executor;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Nabeel Ahmed
 */
public class PrioritizedTask implements Runnable, Comparable<PrioritizedTask> {

    private final String taskId = UUID.randomUUID().toString();
    private final int priority;
    private final Runnable task;
    private final AtomicInteger rejectAttempts = new AtomicInteger(0);

    public PrioritizedTask(int priority, Runnable task) {
        this.priority = priority;
        this.task = task;
    }

    public String getTaskId() {
        return taskId;
    }

    public Runnable getTask() {
        return task;
    }

    public int getPriority() {
        return priority;
    }

    public AtomicInteger getRejectAttempts() {
        return rejectAttempts;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        // Higher priority first
        return Integer.compare(o.priority, this.priority);
    }

    @Override
    public String toString() {
        return "PrioritizedTask{taskId='" + taskId + "', priority=" + priority + "}";
    }
}