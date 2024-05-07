package com.yubi.uls.bulk.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChunkConfig extends BatchConfig{
    private State status = State.PENDING;
    private int offset;
    private int size;
}
