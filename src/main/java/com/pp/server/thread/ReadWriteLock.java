package com.pp.server.thread;

public class ReadWriteLock {
	
	private int readers = 0;
	private int writers = 0;
	private int writeRequests = 0;
	
	public synchronized void lockRead() {
		while(writers>0 || writeRequests> 0){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		readers++;
	}

	public synchronized void unlockRead(){
		readers--;
		notifyAll();
	}

	public synchronized void lockWrite(){
		writeRequests++;
		while (writers>0|| readers>0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writeRequests--;
		writers++;
	}

	public synchronized void unlockWrite(){
		writers--;
		notifyAll();
	}

}
