package com.yubi.uls.bulk.core.client.temporal.impl;

import com.yubi.uls.bulk.core.client.Processor;
import com.yubi.uls.bulk.core.client.Reader;
import com.yubi.uls.bulk.core.client.Writer;
import com.yubi.uls.bulk.core.client.temporal.ChunkProcessorActivity;
import com.yubi.uls.bulk.core.dto.ChunkConfig;
import com.yubi.uls.bulk.core.temporal.JobWorkflow;
import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.workflow.Workflow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Slf4j
public class ChunkProcessorActivityImpl<R,P> implements ChunkProcessorActivity {

    private Reader<R> reader;
    private Processor<R,P> processor;
    private Writer<R,P> writer;

    public void processChunk(ChunkConfig chunkConfig) {
       log.info("Processing chunk offset: {}" ,chunkConfig.getOffset());
        validate();
        List<R> readerData = null;
        List<P> processedData = null;

         readerData = reader.read(chunkConfig);
        if(processor != null) {
              processedData = processor.process(readerData);
        }
        if(writer != null) {
            writer.write(readerData, processedData);
        }
       log.info("Chunk offset processed: {}" ,chunkConfig.getOffset());
    }

    private void validate() {
        if(reader == null) {
            throw new IllegalArgumentException("Reader is required");
        }
    }
}
