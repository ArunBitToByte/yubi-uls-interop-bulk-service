package com.yubi.uls.bulk.service;

import com.yubi.uls.bulk.core.JobLauncher;
import com.yubi.uls.bulk.factory.JobLauncherFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class JobStarter {

    @Value("${bulkservice.workflow.job.queue}")
    private  String TASK_QUEUE;

    private final JobLauncherFactory jobLauncherFactory;
    public  void start() {
        JobLauncher jobLauncher =  jobLauncherFactory.getJobLauncher(null);
        List<String> jobIds = getJobIds();
        for (String jobId : jobIds) {
            jobLauncher.launchJob(jobId);
        }
    }

    private List<String> getJobIds() {
        return  List.of("1");
    }
}
