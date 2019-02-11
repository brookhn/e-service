package com.pp.server.thread;

import java.util.concurrent.Callable;

public class Task implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {
		System.out.println("子线程正在执行");
		Thread.sleep(3000);
		int sum = 100;
		for(int i=0; i<100;i++)
			sum+=i;
		return sum;
	}

}
