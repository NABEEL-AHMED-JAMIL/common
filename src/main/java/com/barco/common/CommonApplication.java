package com.barco.common;

import com.barco.common.manager.async.executor.AsyncDALTaskExecutor;
import com.barco.common.utility.BarcoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PreDestroy;

/**
 * @author Nabeel Ahmed
 */
@SpringBootApplication
public class CommonApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonApplication.class);

	@Autowired
	private AsyncDALTaskExecutor asyncDALTaskExecutor;

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@PreDestroy
	public void onExit() {
		LOGGER.info("Common Application is shutting down, cleaning up resources...");
		if (!BarcoUtil.isNull(asyncDALTaskExecutor)) {
			LOGGER.info("Shutting down AsyncDALTaskExecutor...");
			this.asyncDALTaskExecutor.shutdown();
			LOGGER.info("AsyncDALTaskExecutor shutdown complete.");
		}
		LOGGER.info("Common Application shutdown complete.");
	}

}