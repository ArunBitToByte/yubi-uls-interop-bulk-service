package com.yubi.uls.bulk.core.autoconfigure;

import com.yubi.uls.bulk.core.service.JobWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreAutoConfigure {


    @Bean
    public JobWorker jobWorker() {
        return new JobWorker();
    }





}