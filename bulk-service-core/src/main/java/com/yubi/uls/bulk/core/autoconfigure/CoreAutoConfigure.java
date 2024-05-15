package com.yubi.uls.bulk.core.autoconfigure;


import com.yubi.uls.bulk.core.JobLauncher;
import com.yubi.uls.bulk.core.factory.JobLauncherFactory;
import com.yubi.uls.bulk.core.impl.DefaultJobLauncherImpl;
import com.yubi.uls.bulk.core.service.JobStarter;
import com.yubi.uls.bulk.core.service.JobWorker;
import com.yubi.uls.bulk.repository.JobRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreAutoConfigure {

    @Bean
    public JobStarter jobStarter(JobLauncherFactory jobLauncherFactory){
        return new JobStarter(jobLauncherFactory);
    }

    @Bean
    public JobWorker jobWorker() {
        return new JobWorker();
    }

    @Bean
    public JobLauncherFactory jobLauncherFactory(DefaultJobLauncherImpl jobLauncher){
        return new JobLauncherFactory(jobLauncher);
    }

    @Bean
    public DefaultJobLauncherImpl defaultJobLauncherImpl(JobRepository jobRepository){
        return new DefaultJobLauncherImpl(jobRepository);
    }

}