package com.yubi.uls.bulk.core.client.temporal.impl;

import com.yubi.uls.bulk.core.client.ItemProcessor;
import com.yubi.uls.bulk.core.client.Processor;
import com.yubi.uls.bulk.core.client.Reader;
import com.yubi.uls.bulk.core.client.Writer;
import com.yubi.uls.bulk.core.client.temporal.ChunkProcessorActivity;
import com.yubi.uls.bulk.core.dto.ChunkConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Builder
@Slf4j
public class DefaultChunkProcessorActivityImpl<R> implements ChunkProcessorActivity {

    private ItemProcessor<R> itemProcessor;


    public void processChunk(ChunkConfig chunkConfig) {
       log.info("Processing chunk offset: {}" ,chunkConfig.getOffset());
        validate();
        List<R> data = null;
        data = itemProcessor.process(chunkConfig);
       log.info("Chunk offset processed: {}" ,chunkConfig.getOffset());
    }

    private void validate() {
        if(itemProcessor == null) {
            throw new IllegalArgumentException("itemProcessor is required");
        }
    }
}
