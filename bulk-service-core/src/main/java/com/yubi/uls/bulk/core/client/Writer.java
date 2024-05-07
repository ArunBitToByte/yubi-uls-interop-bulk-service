package com.yubi.uls.bulk.core.client;

import java.util.List;

public interface Writer<I,P> {
    public void write(List<I> sourceItem, List<P> processedItem);
}
