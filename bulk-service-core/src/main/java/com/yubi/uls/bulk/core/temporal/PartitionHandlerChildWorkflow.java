package com.yubi.uls.bulk.core.temporal;

import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.dto.Partition;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PartitionHandlerChildWorkflow {
    @WorkflowMethod
    void handlePartition(Partition jobConfiguration);
}
