package com.yubi.uls.bulk.core.client;

import com.yubi.uls.bulk.core.dto.ChunkConfig;

import java.util.List;

public interface ItemProcessor<T> {
    public List<T> process(ChunkConfig chunk);

}
