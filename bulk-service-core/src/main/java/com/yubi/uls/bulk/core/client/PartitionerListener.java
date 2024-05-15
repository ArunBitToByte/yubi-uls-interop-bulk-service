package com.yubi.uls.bulk.core.client;
public interface PartitionerListener {
    void onStart(Object object);
    void onEnd(Object object);
    void onError(Exception object);

}
