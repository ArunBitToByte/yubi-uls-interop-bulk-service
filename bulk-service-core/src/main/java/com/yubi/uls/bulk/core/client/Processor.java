package com.yubi.uls.bulk.core.client;


import java.util.List;

public interface Processor<T,R> {
    public List<R> process(List<T> item);
}
