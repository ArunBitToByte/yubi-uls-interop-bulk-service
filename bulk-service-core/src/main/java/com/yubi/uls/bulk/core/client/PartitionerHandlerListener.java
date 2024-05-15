package com.yubi.uls.bulk.core.client;
public interface PartitionerHandlerListener {
     void onStart(Object object);
     void onEnd(Object object);
     void onError(Exception object);

}
