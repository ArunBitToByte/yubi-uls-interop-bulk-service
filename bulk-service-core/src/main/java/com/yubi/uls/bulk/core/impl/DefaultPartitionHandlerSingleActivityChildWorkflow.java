package com.yubi.uls.bulk.core.impl;

import com.yubi.uls.bulk.core.client.temporal.ChunkProcessorActivity;
import com.yubi.uls.bulk.core.dto.*;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import com.yubi.uls.bulk.core.temporal.PartitionHandlerChildWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;

@Slf4j
public class DefaultPartitionHandlerSingleActivityChildWorkflow implements PartitionHandlerChildWorkflow{
    @Override
    public void handlePartition(Partition partition) {
        log.info("Handling partition: {}", partition);
        String activityTaskQueue = (String) partition.getParameters().get(ConfigParams.PARTITION_ACTIVITY_QUEUE.name());
        ChunkProcessorActivity chunkProcessorActivity = Workflow.newActivityStub(
                ChunkProcessorActivity.class,
                ActivityOptions.newBuilder().setTaskQueue(activityTaskQueue).setStartToCloseTimeout(Duration.ofSeconds(3600)).build());
        log.info("calling chunk activity for with queue: {}", activityTaskQueue);
        int partitionSize = (int) partition.getParameters().get(ConfigParams.PARTITION_SIZE.name());
        int chunkSize = (int) partition.getParameters().get(ConfigParams.CHUNK_SIZE.name());
        int noOfChunks = partitionSize / chunkSize;

        for( int offset = 0; offset < noOfChunks; offset++) {
            log.info("calling chunk activity for offset: {}", offset);
          chunkProcessorActivity.processChunk(getChunkConfig(partition, offset, chunkSize));
            log.info("chunk activity called for offset: {}", offset);
         }

        reportProgressToParentWorkflow(partition);
       log.info("Partition handled: {}", partition);
    }

    private ChunkConfig getChunkConfig(Partition partition, int offset, int chunkSize){
        return ChunkConfig.builder().offset(offset).size((int)partition.getParameters().get(ConfigParams.CHUNK_SIZE.name())).parameters(partition.getParameters()).status(State.PENDING).build();
    }

    private static void reportProgressToParentWorkflow(Partition partition) {
        Optional<String> parentWorkflowId = Workflow.getInfo().getParentWorkflowId();
        if (parentWorkflowId.isPresent()) {
            String parentId = parentWorkflowId.get();
            JobWorkflow workflow =
                    Workflow.newExternalWorkflowStub(JobWorkflow.class, parentId);
            // Notify parent about record processing completion
            workflow.reportCompletion(partition);
        }
    }
}
