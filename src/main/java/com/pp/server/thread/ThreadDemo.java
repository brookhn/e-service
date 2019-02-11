package com.pp.server.thread;


public class ThreadDemo implements Runnable
{
	
	boolean isRun = true;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRun)
		{
			System.out.println("running......");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
