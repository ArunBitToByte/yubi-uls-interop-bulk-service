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


//    private List<JobConfiguration> createPartitionDummy(JobConfiguration jobConfiguration) throws SQLException {
//        log.info("Partitioning the file for job: {}", jobConfiguration.getParameters().get(ConfigParams.JOB_NAME.name()));
//        int fileCount = 10;//readFileCount(connection);
//        String filePath = (String) jobConfiguration.getParameters().get(ConfigParams.FILE_PATH.name());
//        int chunkSize = (int) jobConfiguration.getParameters().get(ConfigParams.CHUNK_SIZE.name());
//        int partitionCount = fileCount / chunkSize;
//        List<JobConfiguration> offsets = new ArrayList<>();
//        for (int i = 0; i < partitionCount; i++) {
//            Map<String, Object> parameters = new HashMap<>(jobConfiguration.getParameters());
//            JobConfiguration jobConfiguration1 = JobConfiguration.builder().parameters(parameters).build();
////            jobConfiguration.getParameters().put(ConfigParams.PARTITION.name(), new Partition(filePath, i * chunkSize, chunkSize));
//            offsets.add(jobConfiguration1);
//        }
//        log.info("Partitioning successful for job: {}", jobConfiguration.getParameters().get(ConfigParams.JOB_NAME.name()));
//        return offsets;
//    }

//    public List<Partition> split(String FilePath, int partitionSize) {
//        ActivityExecutionContext ctx = Activity.getExecutionContext();
//        JobWorkflow workflow =
//                client.newWorkflowStub(JobWorkflow.class, ctx.getInfo().getWorkflowId());
//        int currentLen = 0;
//        int count = 1, data;
//        List<Partition> partitions = new ArrayList<>();
//        try {
//
//            InputStream infile = new BufferedInputStream(new FileInputStream(filename));
//            data = infile.read();
//            while (data != -1) {
//                log.info("Creating partition: {}", count);
//                filename = new File(basePath+"/split/"+name+"_"+count + ".csv");
//                //RandomAccessFile outfile = new RandomAccessFile(filename, "rw");
//                OutputStream outfile = new BufferedOutputStream(new FileOutputStream(filename));
//                while (data != -1 && currentLen < partitionSize) {
//                    outfile.write(data);
//                    currentLen++;
//                    data = infile.read();
//                }
//
//                Partition partition = Partition.builder().parameters(new HashMap<>()).status(State.PENDING).partitionIndex(count).partitionSize(partitionSize).build();
//                partition.getParameters().put(ConfigParams.PARTITION_FILE_PATH.name(), filename.getAbsolutePath());
//                partitions.add(partition);
//                outfile.close();
//                workflow.reportPartitionProgress(count);
//                currentLen = 0;
//                log.info("Partition created: {}", count);
//                count++;
//            }
//            log.info("Total partitions created: {}", partitions.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return partitions;
//    }




}
