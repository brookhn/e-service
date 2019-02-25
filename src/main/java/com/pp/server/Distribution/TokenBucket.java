package com.pp.server.Distribution;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    private final static byte A_CHAR = 'a';

    public TokenBucket(){}

    public TokenBucket(int maxFlowRate, int avgFlowRate)
    {
        this.avgFlowRate = avgFlowRate;
        this.maxFlowRate = maxFlowRate;
    }

    public void addTokens(Integer tokenNum)
    {
        for (int i = 0 ;i <tokenNum; i++)
        {
            tokenQueue.offer(Byte.valueOf(A_CHAR));
        }
    }

    public void start(){
        if (maxFlowRate > 0)
        {
            tokenQueue = new ArrayBlockingQueue<>(maxFlowRate);
        }
        TokeProduct tokeProduct = new TokeProduct(avgFlowRate, this);
        scheduledExecutorService.scheduleAtFixedRate(tokeProduct,0, 1, TimeUnit.SECONDS);
        started = true;
    }

    public void stop()
    {
        started = false;
        scheduledExecutorService.shutdown();
    }

    public static TokenBucket newBuild()
    {
        return new TokenBucket();
    }

    public void setAvgFlowRate(int avgFlowRate)
    {
        this.avgFlowRate = avgFlowRate;
    }

    public void setMaxFlowRate(int maxFlowRate)
    {
        this.maxFlowRate = maxFlowRate;
    }

    public int getMaxFlowRate()
    {
        return this.maxFlowRate;
    }

    public int getAveFlowRate()
    {
        return this.avgFlowRate;
    }

    public boolean getTokens(byte[] dataSize)
    {
        int needNumToken = dataSize.length / avgFlowRate +1;
        ReentrantLock lock = this.lock;
        try{
            lock.lock();
            boolean result = needNumToken <= tokenQueue.size();
            if (!result)
            {
                return false;
            }
            int fetchTokeNum = 0;
            for (int i = 0 ; i< needNumToken; i++)
            {
                Byte poll = tokenQueue.poll();
                if (poll != null)
                {
                    fetchTokeNum++;
                }
            }
            return needNumToken == fetchTokeNum;
        }catch (Exception e)
        {

        }finally {
            lock.unlock();
        }


        return false;
    }

    public String StringCopy(String data, int copyNum)
    {
        StringBuilder builder = new StringBuilder(copyNum* data.length());
        for (int i = 0; i< copyNum ; i++)
        {
            builder.append(data);
        }
        return builder.toString();
    }


    public class TokeProduct implements  Runnable{
        TokenBucket tokenBucket;

        int avgFlowRate;

        public TokeProduct(int avgFlowRate, TokenBucket tokenBucket)
        {
            this.avgFlowRate = avgFlowRate;
            this.tokenBucket = tokenBucket;
        }

        @Override
        public void run() {
            tokenBucket.addTokens(avgFlowRate);
        }
    }

    public static void main(String args[])
    {
        tokeTest();
    }

    public static void tokeTest()
    {
        TokenBucket tokenBucket = TokenBucket.newBuild();
        tokenBucket.setAvgFlowRate(502);
        tokenBucket.setMaxFlowRate(1024);
        tokenBucket.start();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D://e//test.log")));
            String data = "love";
            for (int i = 0; i < 1000; i++)
            {
                Random random = new Random();
                int ri = random.nextInt(100);
                boolean tokens = tokenBucket.getTokens(tokenBucket.StringCopy(data, ri).getBytes());
                if (tokens)
                {
                    bufferedWriter.write("token pass ------index:"+ri);
                    System.out.println("token pass ------index:"+ri);
                }else{
                    bufferedWriter.write("token reject ------index:"+ri);
                    System.out.println("token reject ------index:"+ri);
                }
                
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
