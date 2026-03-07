package com.barco.common.config;

import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.manager.async.properties.AsyncTaskProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * @author Nabeel Ahmed
 */
@EnableAsync
@Configuration
public class CommonConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);

    public final AsyncTaskProperties asyncTaskProperties;

    public CommonConfig(AsyncTaskProperties asyncTaskProperties) {
        this.asyncTaskProperties = asyncTaskProperties;
    }

    /**
    * Method use to async email task executor
    * @return Executor
    * */
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        LOGGER.info("===============Application-EMAIL-INIT===============");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Email-");
        executor.initialize();
        LOGGER.info("===============Application-EMAIL-END===============");
        return executor;
    }

    /**
     * Method use to async dal task executor
     * @return AsyncDALTaskExecutor
     * */
    @Scope("singleton")
    @Bean(name = "asyncDALTaskExecutor")
    public AsyncDALTaskExecutor asyncDALTaskExecutor() throws Exception {
        LOGGER.info("===============Application-DAO-INIT===============");
        AsyncDALTaskExecutor taskExecutor = new AsyncDALTaskExecutor(
            this.asyncTaskProperties.getCorePoolSize(), this.asyncTaskProperties.getMaxPoolSize(),
            this.asyncTaskProperties.getQueueCapacity(), this.asyncTaskProperties.getKeepAlive());
        LOGGER.info("===============Application-DAO-END===============");
        return taskExecutor;
    }

}