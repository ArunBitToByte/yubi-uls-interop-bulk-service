package com.yubi.uls.bulk.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@SuperBuilder
public class BatchConfig {
     protected Map<String, Object> parameters;

    public BatchConfig() {
        this.parameters = new HashMap<>();
    }
}
