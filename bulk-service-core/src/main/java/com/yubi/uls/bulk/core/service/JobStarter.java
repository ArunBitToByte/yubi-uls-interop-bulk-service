package com.yubi.uls.bulk.core.service;

import com.yubi.uls.bulk.core.dto.ConfigParams;
import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@RequiredArgsConstructor
@Component
public class JobStarter {


    @Value("${file.bulkservice.workflow.job.queue}")
    private String TASK_QUEUE;


    public void launchJob() {
        log.info("Launching job ");
        JobConfiguration jobConfiguration = geJobConfig();
        launchJob(jobConfiguration);
        log.info("job successfully launched");
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

    private JobConfiguration geJobConfig() {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put(ConfigParams.PARTITION_ACTIVITY_QUEUE.name(), TASK_QUEUE);
        parameters.put(ConfigParams.FILE_PATH.name(), "/Users/arunkumar.chouhan/projectHome/rnd/bulk_processing/data/sample_dataset.csv");
        parameters.put(ConfigParams.CHUNK_SIZE.name(), 100);
        parameters.put(ConfigParams.MAX_CONCURRENCY.name(), 1);
        parameters.put(ConfigParams.PARTITION_SIZE.name(), 10000);
        parameters.put(ConfigParams.PARTITION_QUERY.name(), "select * from table");
        return JobConfiguration.builder().parameters(parameters).build();
    }
}
