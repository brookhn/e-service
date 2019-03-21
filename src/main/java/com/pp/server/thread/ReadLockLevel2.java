package com.pp.server.thread;

import java.util.HashMap;
import java.util.Map;

public class ReadLockLevel2 {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    Map<Thread, Integer> readThreads = new HashMap<>();
    

    private Thread writeThread = null;

    public synchronized void lockRead()
    {
        Thread cuurentThread = Thread.currentThread();
        while(!canGrandReadAccess(cuurentThread))
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        readThreads.put(cuurentThread, getReadAccessCount(cuurentThread) +1);
    }

    public synchronized void unlockRead()
    {
        Thread cuurentThread = Thread.currentThread();
        Integer readNum = getReadAccessCount(cuurentThread);
        if (readNum == 1)
        {
            readThreads.remove(cuurentThread);
        }else{
            readThreads.put(cuurentThread, getReadAccessCount(cuurentThread) -1 );
        }
        notifyAll();
    }

    public synchronized void unlockWrite()
    {
        Thread cuurentThread = Thread.currentThread();
        writers--;
        if (writers == 0) {
            writeThread = null;
        }
        notifyAll();
    }

    public synchronized void lockWrite()
    {
        Thread currentThread = Thread.currentThread();
        writeRequests++;
        while(!canWriteAccess(currentThread))
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeRequests--;
        writers++;
        writeThread = currentThread;
    }

    public boolean canGrandReadAccess(Thread callingThread)
    {
        if (isWriter(callingThread))
        {
            return true;
        }
        if(writers > 0) {
            return false;
        }
        if (isReading(callingThread))
        {
            return true;
        }

        if (writeRequests > 0)
        {
            return false;
        }
        return true;
    }

    private int getReadAccessCount(Thread callingThread)
    {
        Integer accessCount = readThreads.get(callingThread);
        if (accessCount == null) {
            return 0;
        }
        return accessCount;
    }

    private boolean isReading(Thread callingThread)
    {
        return readThreads.get(callingThread) != null;
    }

    public boolean canWriteAccess(Thread callingThread)
    {
        if (isOnlyRead(callingThread))
        {
            return true;
        }

        if(hasReads()) {
            return false;
        }
        if (writeThread == null)
        {
            return true;
        }
        if (isWriter(callingThread))
        {
            return false;
        }
        return true;
    }

    public boolean hasReads()
    {
        return readThreads.size()>0;
    }

    private boolean isWriter(Thread callingThread){
        return writeThread == callingThread;
    }

    private boolean isOnlyRead(Thread callingThread)
    {
        return  (readers==1&& readThreads.get(callingThread) != null);
    }

    public static void main(String args[])
    {
        ReadLockLevel2 readLockLevel2 = new ReadLockLevel2();
        readLockLevel2.lockRead();
        readLockLevel2.unlockRead();
    }
}
