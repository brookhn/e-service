package com.pp.server.thread;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V> {

	private final int maxCapacity;
	
	public static final float LOAD_FACTORY = 0.75f;
	
	private final Lock lock = new ReentrantLock();
	
	public LRULinkedHashMap(int maxCapacity) {
		super(maxCapacity, LOAD_FACTORY, true);
		this.maxCapacity = maxCapacity;
	}
	
	public V get(Object key)
	{
		try {
			lock.lock();
			return super.get(key);
		}finally {
			lock.unlock();
		}
	}
	
	public V put(K key, V value)
	{
		try {
			lock.lock();
			return super.put(key, value);
		}finally {
			lock.unlock();
		}
	}
	
	public int size() {
		try {
			lock.lock();
			return super.size();
		}finally {
			lock.unlock();
		}
	}
	
	public boolean containsKey(Object key) {
		try {
			lock.lock();
			return super.containsKey(key);
		}finally {
			lock.unlock();
		}
	}
	
	
}
