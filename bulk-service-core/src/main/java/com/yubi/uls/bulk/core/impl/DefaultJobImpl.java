package com.yubi.uls.bulk.core.impl;

import com.yubi.uls.bulk.core.client.temporal.PartitionerActivity;
import com.yubi.uls.bulk.core.dto.Partition;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import com.yubi.uls.bulk.core.dto.BatchProgress;
import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.dto.ConfigParams;
import com.yubi.uls.bulk.core.temporal.PartitionHandlerChildWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//
@Slf4j
public class DefaultJobImpl implements JobWorkflow {

    public String getName() {
        return null;
    }
    private final BatchProgress batchProgress = new BatchProgress();
    private final Set<Partition> runningWorkflows = new HashSet<>();

    @Override
    public void process(JobConfiguration jobConfiguration) {
        try{
            log.info("Processing job configuration: {}", jobConfiguration);
            //run prehooks
            //run job
            processJob(jobConfiguration);
            //run posthooks
            log.info("job completed successfully, configuration: {}", jobConfiguration);
        }catch (Exception e){
            log.error("Error processing job configuration: {}", jobConfiguration, e);
        }
    }

    private void processJob(JobConfiguration jobConfiguration) {
        List<Partition> partitions  = getPartitions(jobConfiguration);
        batchProgress.initialize(partitions);
        processPartitions(partitions, jobConfiguration);
    }

    private List<Partition> getPartitions(JobConfiguration jobConfiguration) {
        String activityTaskQueue = (String) jobConfiguration.getParameters().get(ConfigParams.PARTITION_ACTIVITY_QUEUE.name());
        PartitionerActivity partitionerActivity = Workflow.newActivityStub(
                PartitionerActivity.class,
                ActivityOptions.newBuilder().setTaskQueue(activityTaskQueue).setStartToCloseTimeout(Duration.ofSeconds(3600)).build());
        log.info("calling partition activity for with queue: {}", activityTaskQueue);
        List<Partition> partitions =  partitionerActivity.partition(jobConfiguration);
        log.info("received partitions with total partition size: {}", partitions.size());
        return partitions;
    }

    private void processPartitions(List<Partition> partitions, JobConfiguration jobConfiguration) {

        int partitionIndex = 0;

        for (Partition partition : partitions) {
            partitionIndex = processPartition(jobConfiguration, partitionIndex, partition);
        }
        Workflow.await(runningWorkflows::isEmpty);
    }

    private int processPartition(JobConfiguration jobConfiguration, int partitionIndex, Partition partition) {
        int maxConcurrency = (int) jobConfiguration.getParameters().get(ConfigParams.MAX_CONCURRENCY.name());
        String childId = Workflow.getInfo().getWorkflowId() + "/" + partitionIndex++;
        PartitionHandlerChildWorkflow childWorkflow = Workflow.newChildWorkflowStub(
                PartitionHandlerChildWorkflow.class,
                ChildWorkflowOptions.newBuilder().setWorkflowId(childId).build());
        partition.getParameters().putAll(jobConfiguration.getParameters());
        Async.procedure(childWorkflow::handlePartition, partition);
        runningWorkflows.add(partition);
        batchProgress.updateInProgress(partition);
        // After starting MAX_CONCURRENCY children blocks until a completion signal is received.
        Workflow.await(() -> runningWorkflows.size() < maxConcurrency);
        return partitionIndex;
    }


    @Override
    public void reportCompletion(Partition partition) {
        runningWorkflows.remove(partition);
        batchProgress.updateCompletion(partition);
    }

    @Override
    public void reportPartitionProgress(int count) {
        log.info("Partition progress: {}", count);

    }

    @Override
    public BatchProgress getProgress() {
        return batchProgress;
    }
}
