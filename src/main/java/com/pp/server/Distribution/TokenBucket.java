package com.pp.server.Distribution;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {

    private static final int DEFAULT_BUCKET_SIZE = 60*1024*1024;

    private int everyTokenSize = 1;

    private int maxFlowRate;

    private int avgFlowRate;

    private ArrayBlockingQueue<Byte> tokenQueue = new ArrayBlockingQueue<>(DEFAULT_BUCKET_SIZE);

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private volatile boolean started = false;

    private ReentrantLock lock = new ReentrantLock(true);

    
}
