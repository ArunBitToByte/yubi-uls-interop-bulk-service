package com.yubi.uls.bulk.core.client;

import java.util.List;

public interface ItemProcessorWithDuckDBReader {

    public void process(List<String> chunk);
}
