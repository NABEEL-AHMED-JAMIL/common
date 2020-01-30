package com.barco.common.manager.async.properties;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel.amd
 */
@Component
@Scope("prototype")
public class AsyncTaskProperties {

    @Value("${asynce.task.executor.minThreads}")
    private Integer minThreads;
    @Value("${asynce.task.executor.maxThreads}")
    private Integer maxThreads;
    @Value("${asynce.task.executor.idleThreadLife}")
    private Integer idleThreadLife;

    public AsyncTaskProperties() { }

    public Integer getMinThreads() { return minThreads; }
    public void setMinThreads(Integer minThreads) { this.minThreads = minThreads; }

    public Integer getMaxThreads() { return maxThreads; }
    public void setMaxThreads(Integer maxThreads) { this.maxThreads = maxThreads; }

    public Integer getIdleThreadLife() { return idleThreadLife; }
    public void setIdleThreadLife(Integer idleThreadLife) { this.idleThreadLife = idleThreadLife; }

    @Override
    public String toString() { return new Gson().toJson(this); }

}
