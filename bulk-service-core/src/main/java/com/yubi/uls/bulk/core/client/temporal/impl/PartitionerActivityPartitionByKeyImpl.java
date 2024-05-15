package com.yubi.uls.bulk.core.client.temporal.impl;




import com.yubi.uls.bulk.core.client.Partitioner;
import com.yubi.uls.bulk.core.client.temporal.PartitionerActivity;
import com.yubi.uls.bulk.core.dto.ConfigParams;
import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.dto.Partition;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
public class PartitionerActivityPartitionByKeyImpl implements PartitionerActivity {

    private Partitioner partitioner;
    private Connection connection;

    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    private static final WorkflowClient client = WorkflowClient.newInstance(service);

    @Override
    public List<Partition> partition(JobConfiguration jobConfiguration) {
        try {
            //load file to db
            //partition
            //return partitions
            partitioner.init(jobConfiguration);
            return createPartition( jobConfiguration);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Partition> createPartition(JobConfiguration jobConfiguration) throws SQLException {
        log.info("Partitioning the file for job: {}", jobConfiguration.getParameters().get(ConfigParams.JOB_NAME.name()));
        ActivityExecutionContext ctx = Activity.getExecutionContext();
        JobWorkflow workflow = client.newWorkflowStub(JobWorkflow.class, ctx.getInfo().getWorkflowId());
        List<Partition> partitions= new ArrayList<>();
        Partition partition;
        while(( partition = partitioner.getNextPartition()) != null) {
            partitions.add(partition);
            workflow.reportPartitionProgress(partition.getPartitionIndex());
        }
        log.info("Partitioning successful for job: {}", jobConfiguration.getParameters().get(ConfigParams.JOB_NAME.name()));
        return partitions;
    }





}
