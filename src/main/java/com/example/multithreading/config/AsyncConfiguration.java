package com.example.multithreading.config;

import java.util.concurrent.Executor;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

	private static final org.jboss.logging.Logger LOGGER = LoggerFactory.logger(AsyncConfiguration.class);

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		LOGGER.info("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//configure no of thread
		executor.setCorePoolSize(3);
		// configure maximum no of thred
		executor.setMaxPoolSize(3);
		executor.setQueueCapacity(100);
		// name of the thread
		executor.setThreadNamePrefix("CarThread-");
		// initilizes the git push --set-upstream origin master
		executor.initialize();
		return executor;
	}
}
