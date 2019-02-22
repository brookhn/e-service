//package com.pp.server.thread;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.ReentrantLock;
//
//import javax.validation.constraints.AssertTrue;
//
//import junit.framework.Assert;
//
//public class BloomFilter {
//	private int arraySize;
//
//	private int[] array;
//
//	public BloomFilter(int arraySize) {
//		this.arraySize = arraySize;
//		array = new int[arraySize];
//	}
//
//	public void add(String key) {
//		int first = hashcode_1(key);
//		int second = hashcode_2(key);
//		int three = hashcode_3(key);
//		array[first%arraySize] = 1;
//		array[second%arraySize] = 1;
//		array[three%arraySize] = 1;
//	}
//
//	public boolean check(String key) {
//		int first = hashcode_1(key);
//		int second = hashcode_2(key);
//		int three = hashcode_3(key);
//
//		int value = array[first %arraySize];
//
//		if (value == 0) {
//			return false;
//		}
//
//		value = array[second % arraySize];
//		if (value == 0) {
//			return false;
//		}
//
//		value = array[three % arraySize];
//		if (value == 0) {
//			return false;
//		}
//
//		return true;
//	}
//
//	private int hashcode_1(String key)
//	{
//		int hash = 0;
//		for(int i = 0; i< key.length(); i++)
//		{
//			hash = 33* hash+key.charAt(i);
//		}
//		return Math.abs(hash);
//	}
//
//	private int hashcode_2(String key)
//	{
//		final int p = 16777619;
//		int hash = (int)2166136261L;
//		for(int i =0; i< key.length();i++)
//		{
//			hash = (hash^key.charAt(i))*p;
//		}
//		hash += hash<<13;
//		hash ^= hash>>7;
//		hash += hash<<3;
//		hash ^= hash>>17;
//		hash += hash<<5;
//		return Math.abs(hash);
//	}
//
//	private int hashcode_3(String key)
//	{
//		int hash,i;
//		for(hash =0,i=0; i< key.length();i++)
//		{
//			hash += key.charAt(i);
//			hash += (hash<<0);
//			hash ^= (hash>>6);
//		}
//		hash += (hash<<3);
//		hash ^= (hash>>11);
//		hash += (hash<<15);
//		return Math.abs(hash);
//	}
//
//	public static void main(String args[]) {
//
////		ReentrantLock reentrantLock = new ReentrantLock();
//		ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
////		long start = System.currentTimeMillis();
////		BloomFilter bloomFilter = new BloomFilter(100000);
////		for(int i =0; i<100000;i++)
////		{
////			bloomFilter.add(i+"");
////		}
//
//		Map<String, Object> testMap = new HashMap<>();
//
//		testMap.put("1A", "test1");
//		testMap.put("2A", "test2");
//		testMap.put("3A", "test3");
//		testMap.put("4A", "test4");
//		testMap.put("5A", "test5");
//		testMap.put("6A", "test6");
//
//		Iterator<Entry<String, Object>> i = testMap.entrySet().iterator();
//		Entry<String, Object> correctEntry = null;
//		while(correctEntry == null && i.hasNext())
//		{
//			    Entry<String, Object> e = i.next();
//				if ("4A".equals(e.getKey())) {
//					correctEntry = e;
//				}
//		}
//		i.remove();
//		testMap.forEach((key,value)->{
//			System.out.println(key);
//		});
//		/**
//		 * 获取CPU核数
//		 */
//		int cpus = Runtime.getRuntime().availableProcessors();
//		System.out.println(cpus);
//		//boolean result = bloomFilter.check(5644+"");
//		//System.out.println("result:"+result);
//	}
//
//}
