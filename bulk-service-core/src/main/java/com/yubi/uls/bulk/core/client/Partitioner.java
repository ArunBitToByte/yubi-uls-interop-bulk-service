package com.yubi.uls.bulk.core.client;

import com.yubi.uls.bulk.core.dto.JobConfiguration;
import com.yubi.uls.bulk.core.dto.Partition;

import java.util.List;


public interface Partitioner {
    void init(JobConfiguration jobConfiguration);
    Partition getNextPartition();
}
