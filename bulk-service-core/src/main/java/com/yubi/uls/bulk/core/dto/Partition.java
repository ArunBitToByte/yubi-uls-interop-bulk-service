package com.yubi.uls.bulk.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Partition extends BatchConfig{

    private State status = State.PENDING;
    private int partitionIndex;
    private int partitionSize;

}
