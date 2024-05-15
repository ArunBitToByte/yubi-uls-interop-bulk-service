package com.yubi.uls.bulk.core.service;

import com.yubi.uls.bulk.core.client.temporal.impl.PartitionerActivityImpl;
import com.yubi.uls.bulk.core.client.temporal.impl.PartitionerDuckDbImpl;
import com.yubi.uls.bulk.core.impl.DefaultJobImpl;
import com.yubi.uls.bulk.core.impl.DefaultPartitionHandlerChildWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobWorker {

    @Value("${bulkservice.workflow.job.queue}")
    private  String TASK_QUEUE;

    public void run() {
        log.info("Running bulk job worker");
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(
                DefaultJobImpl.class, DefaultPartitionHandlerChildWorkflow.class);
        worker.registerActivitiesImplementations(PartitionerActivityImpl.builder().partitioner(new PartitionerDuckDbImpl()).build());
        factory.start();
        log.info("Worker started for task queue: {}", TASK_QUEUE);

    }

}
