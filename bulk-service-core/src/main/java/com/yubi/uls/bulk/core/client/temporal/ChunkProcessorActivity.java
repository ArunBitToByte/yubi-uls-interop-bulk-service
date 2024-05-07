package com.yubi.uls.bulk.core.client.temporal;

import com.yubi.uls.bulk.core.client.Processor;
import com.yubi.uls.bulk.core.client.Reader;
import com.yubi.uls.bulk.core.client.Writer;
import com.yubi.uls.bulk.core.dto.ChunkConfig;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;

@ActivityInterface
public interface ChunkProcessorActivity {


    @ActivityMethod
    public void processChunk(ChunkConfig chunkConfig);
}
