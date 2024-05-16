package com.yubi.uls.file.bulk.impl;

import com.yubi.uls.bulk.core.client.ItemProcessor;
import com.yubi.uls.bulk.core.client.ItemProcessorWithDuckDBReader;
import com.yubi.uls.bulk.core.dto.ChunkConfig;
import com.yubi.uls.bulk.core.dto.ConfigParams;
import com.yubi.uls.file.bulk.dto.Item;
import lombok.extern.slf4j.Slf4j;
import org.duckdb.DuckDBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemProcessorImpl implements ItemProcessorWithDuckDBReader {
    @Override
    public void process(List<String> chunkData) {
        log.info("Processing chunk completed: {}", chunkData.get(0));
    }

}
