package com.pp.server.rule;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAprior {

    public static int  loops = 0; //轮询次数
    public static double  MIN_SUPPORT = 0.02;
    public static double MIN_CONFIDENCE = 0.6;
    public static boolean endTag = false; //结束标志
    public static List<List<String>> dataSet = new ArrayList<List<String>>();
    public static List<List<String>> frequentItemSet = new ArrayList<>();
    static List<MyMap> map = new ArrayList<>();


    public void load(String url)  {
        if (null == url) {
            return;
        }
        BufferedReader read = null;
        try {
            File file = new File(url);
            if (!file.exists()) {
                return;
            }
            read = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String readLine = null;
            while (null != (readLine = read.readLine())) {
                String[] lineAry = readLine.split(",");
                List<String> list = new ArrayList<>();
                for (String line : lineAry) {
                    list.add(line);
                }
                dataSet.add(list);
            }
        }catch (IOException ie)
        {
            ie.printStackTrace();
        }finally {
            try {
                if (null != read)
                {
                    read.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void show(List<List<String>> shList)
    {
        shList.forEach(value->{
            value.forEach(invalue->{
                System.out.print(invalue+" ");
            });
            System.out.println();
        });
    }

    public List<List<String>> nxtCandicate(List<List<String>> frequentItemSet)
    {
//        frequentItemSet.forEach(value->{
//            HashSet<String> hSet = new HashSet<>();
//            HashSet<String> hSetCpy = new HashSet<>();
//            value.forEach(inValue->{
//                hSet.add(inValue);
//            });
//            int hLen_Bfr = hSet.size();
//            hSetCpy = (HashSet<String>) hSet.clone();
//            frequentItemSet.forEach(rvalue->{
//                hSet = hSetCpy.clone();
//            });
//        });
        return null;
    }

    public static boolean notHave(HashSet<String> hset, List<List<String>> cmpL)
    {

        return false;
    }

    //剪枝
    public  List<List<String>> prune(List<List<String>> pruneL)
    {
        AtomicBoolean end = new AtomicBoolean(true);
        List<List<String>> supportL = new ArrayList<>();
        pruneL.forEach(value->{
            int num = statisticsFrequence(value);
            if (num >= MIN_SUPPORT * (dataSet.size() -1))
            {
                supportL.add(value);
                map.add(new MyMap(value, num));
                end.compareAndSet(true, false);
            }
        });
        endTag = end.get();
        if (endTag)
            System.out.println("无法剪树枝");
        return supportL;
    }

    public static int statisticsFrequence(List<String> statisL)
    {
        AtomicInteger num = new AtomicInteger(0);
        dataSet.forEach(value->{
            AtomicBoolean notEqual = new AtomicBoolean(false);
            statisL.forEach(svalue->{
                if (!value.contains(svalue))
                {
                    notEqual.compareAndSet(false, true);
                    return;
                }
            });
            if (!notEqual.get())
            {
                num.incrementAndGet();
            }
        });
        return num.get();
    }

    /**
     * 重新组合
     * @return
     */
    public List<List<String>> recom()
    {
        List<List<String>> tableL = new ArrayList<>();
        HashSet<String> hset = new HashSet<String>();
        dataSet.forEach(value->{
            value.forEach(inValue->{
                hset.add(inValue);
            });
        });
        hset.forEach(value->{
            List<String> tmpL = new ArrayList<>();
            tmpL.add(value);
            tableL.add(tmpL);
        });
        return tableL;
    }


    public static void main(String args[])
    {
        MyAprior myAprior = new MyAprior();
        myAprior.load("D:\\e\\top1000data");
        List<List<String>> reList = myAprior.recom();
        List<List<String>> prueL = myAprior.prune(reList);
        myAprior.show(prueL);

//        LinkedList<String> TCollection = new LinkedList<>();
//        TCollection.add("bruce");
//        TCollection.add("brook");
//
//        LinkedList<String> ETCollection = new LinkedList<>();
//        ETCollection.add("bruce");
//        ETCollection.add("brook");
//
//        MyMap myMap = myAprior.new MyMap(TCollection, 2);
//        boolean result = myMap.equas(ETCollection);
//        System.out.println("cmpResult: "+ result);
    }

    public class MyMap{
        private List<String> collection = new LinkedList<>();
        private int quota =0;

        public MyMap(List<String> collection, int quota)
        {
            this.collection = collection;
            this.quota = quota;
        }

        public boolean equas(LinkedList<String> in)
        {
            if (in.size() != collection.size())
            {
                return false;
            }

            AtomicBoolean cmpReuslt = new AtomicBoolean(true);

            in.forEach(value->{
                if (!collection.contains(value))
                {
                  cmpReuslt.compareAndSet(true, false);
                  System.out.println("value: "+value);
                  return;
                }
            });
            return cmpReuslt.get();
        }
    }
}
