package com.pp.server.thread;

public class BloomFilter {
	private int arraySize;
	
	private int[] array;
	
	public BloomFilter(int arraySize) {
		this.arraySize = arraySize;
		array = new int[arraySize];
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
		return 0;
	}
	
	
}
