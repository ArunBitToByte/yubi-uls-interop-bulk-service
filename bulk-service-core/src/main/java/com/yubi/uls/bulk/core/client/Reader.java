package com.yubi.uls.bulk.core.client;

import com.yubi.uls.bulk.core.dto.ChunkConfig;
import com.yubi.uls.bulk.core.dto.JobConfiguration;

import java.util.Iterator;
import java.util.List;

public interface Reader<T> {
   public List<T> read(ChunkConfig chunk);

}
