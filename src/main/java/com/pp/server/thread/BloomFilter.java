package com.pp.server.thread;

public class BloomFilter {
	private int arraySize;
	
	private int[] array;
	
	public BloomFilter(int arraySize) {
		this.arraySize = arraySize;
		array = new int[arraySize];
	}
	
	public void add(String key) {
		int first = hashcode_1(key);
		int second = hashcode_2(key);
		int three = hashcode_3(key);
		array[first%arraySize] = 1;
		array[second%arraySize] = 1;
		array[three%arraySize] = 1;
	}
	
	public boolean check(String key) {
		int first = hashcode_1(key);
		int second = hashcode_2(key);
		int three = hashcode_3(key);
		
		int value = array[first %arraySize];
		
		if (value == 0) {
			return false;
		}
		
		value = array[second % arraySize];
		if (value == 0) {
			return false;
		}
		
		value = array[three % arraySize];
		if (value == 0) {
			return false;
		}
		
		return true;
	}
	
	private int hashcode_1(String key)
	{
		int hash = 0;
		for(int i = 0; i< key.length(); i++)
		{
			hash = 33* hash+key.charAt(i);
		}
		return Math.abs(hash);
	}
	
	private int hashcode_2(String key)
	{
		final int p = 16777619;
		int hash = (int)2166136261L;
		for(int i =0; i< key.length();i++)
		{
			hash = (hash^key.charAt(i))*p;
		}
		hash += hash<<13;
		hash ^= hash>>7;
		hash += hash<<3;
		hash ^= hash>>17;
		hash += hash<<5;
		return Math.abs(hash);
	}
	
	private int hashcode_3(String key)
	{
		int hash,i;
		for(hash =0,i=0; i< key.length();i++)
		{
			hash += key.charAt(i);
			hash += (hash<<0);
			hash ^= (hash>>6);
		}
		hash += (hash<<3);
		hash ^= (hash>>11);
		hash += (hash<<15);
		return Math.abs(hash);
	}
	
	public static void main(String args[]) {
		
	}
	
}
