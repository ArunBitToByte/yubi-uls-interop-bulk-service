package com.yubi.uls.bulk.core.impl;

import com.yubi.uls.bulk.core.JobLauncher;
import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import com.yubi.uls.bulk.core.utility.helper.HelperUtility;
import com.yubi.uls.bulk.entity.JobEntity;
import com.yubi.uls.bulk.repository.JobRepository;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class DefaultJobLauncherImpl implements JobLauncher {

    private final  JobRepository jobRepository;

    @Value("${bulkservice.workflow.job.queue}")
    private String TASK_QUEUE;

    @Override
    public void launchJob(String jobId) {
        log.info("Launching job with id: {}", jobId);
        JobConfiguration jobConfiguration = geJobConfig(jobId);
        launchJob(jobConfiguration);
        log.info("job successfully launched with id: {}", jobId);
    }

    private void launchJob(JobConfiguration jobConfiguration) {
        log.info("Launching job workflow with configuration: {}", jobConfiguration);
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient workflowClient = WorkflowClient.newInstance(service);
        WorkflowOptions options = WorkflowOptions.newBuilder().setTaskQueue(TASK_QUEUE).build();
        JobWorkflow jobWorkflow = workflowClient.newWorkflowStub(JobWorkflow.class, options);
        WorkflowExecution execution = WorkflowClient.start(jobWorkflow::process,
               jobConfiguration);
        log.info("Started job workflow. WorkflowId= {} , RunId= {}",execution.getWorkflowId(),execution.getRunId());
    }

    private JobConfiguration geJobConfig(String jobId) {
        log.info("Fetching job configuration for job id: {}", jobId);
        JobEntity jobEntity = jobRepository.findById(jobId).get();
        log.info("Fetched job configuration for job id: {}", jobId);
//        JobConfiguration jobConfiguration= JobConfiguration.builder().parameters(HelperUtility.convertJsonNodeToMap(jobEntity.getConfig())).build();
        JobConfiguration jobConfiguration= JobConfiguration.builder().parameters(HelperUtility.convertJsonNodeToMap(jobEntity.getConfig())).build();
        return jobConfiguration;
    }
}
