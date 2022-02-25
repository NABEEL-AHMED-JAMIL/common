package com.barco.common.config;

import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.manager.async.properties.AsyncTaskProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Nabeel Ahmed
 */
@Configuration
public class CommonConfig {

    public Logger logger = LogManager.getLogger(CommonConfig.class);

    @Autowired
    public AsyncTaskProperties asyncTaskProperties;

    @Bean
    @Scope("singleton")
    public AsyncDALTaskExecutor asyncDALTaskExecutor() throws Exception {
        logger.debug("===============Application-DAO-INIT===============");
        AsyncDALTaskExecutor taskExecutor = new AsyncDALTaskExecutor(this.asyncTaskProperties.getMinThreads(),
              this.asyncTaskProperties.getMaxThreads(), this.asyncTaskProperties.getIdleThreadLife());
        logger.debug("===============Application-DAO-END===============");
        return taskExecutor;
    }

}