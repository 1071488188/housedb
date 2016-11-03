package com.github.coolcool.sloth.lianjiadb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@Configuration
@EnableAsync
public class SpringExtendConfig {


	@Value("${task.executor.corePoolSize:3}")
	private Integer TaskExecutorcorePoolSize;

	@Value("${task.executor.maxPoolSize:3}")
	private Integer TaskExecutormaxPoolSize;

	/**
	 * 线程池
	 *
	 * @return
	 * @author chenxingwang
	 */
	@Bean
	public AsyncTaskExecutor getAsyncTaskExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setCorePoolSize(TaskExecutorcorePoolSize);
		tpte.setMaxPoolSize(TaskExecutormaxPoolSize);
		tpte.setKeepAliveSeconds(300);
		tpte.setThreadNamePrefix("taskExecutor-");
		return tpte;
	}

	/**
	 * 提任定时任务线程池
	 * @return
	 * @author chenxingwang
	 */
	@Bean
	public TaskScheduler getTaskScheduler() {
		ThreadPoolTaskScheduler tpts = new ThreadPoolTaskScheduler();
		tpts.setPoolSize(4);
		tpts.setThreadNamePrefix("taskScheduler-");
		return tpts;
	}
}
