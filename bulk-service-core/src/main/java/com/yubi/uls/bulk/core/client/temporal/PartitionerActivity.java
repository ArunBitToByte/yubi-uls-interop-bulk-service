package com.yubi.uls.bulk.core.client.temporal;

import com.yubi.uls.bulk.core.client.Partitioner;
import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.dto.Partition;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.List;

@ActivityInterface
public interface PartitionerActivity {
    @ActivityMethod
    List<Partition> partition(JobConfiguration jobConfiguration);
}
