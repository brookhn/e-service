package com.pp.server.thread;

import java.util.HashMap;
import java.util.Map;

public class ReadLockLevel2 {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    Map<Thread, Integer> readThreads = new HashMap<>();

    public synchronized void lockRead()
    {

    }

    public synchronized void unlockRead()
    {

    }

    public boolean canGrandReadAccess(Thread callingThread)
    {

        return false;
    }

}
