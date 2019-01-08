package com.akr.leadIq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class StarterClass {

    public static void main(String[] args) {
        SpringApplication.run(StarterClass.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadExec = new ThreadPoolTaskExecutor();
        threadExec.setCorePoolSize(4);
        threadExec.setMaxPoolSize(6);
        threadExec.setThreadNamePrefix("ImgurREST-");
        threadExec.setQueueCapacity(200);
        threadExec.initialize();
        return threadExec;
    }
}
