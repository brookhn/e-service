package com.pp.server.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BaseThread {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		ThreadDemo demo = new ThreadDemo();
////		//demo.run();
//		Thread myThread = new Thread(demo, "new Thread");
//		myThread.start();
//		ExecutorService executor = Executors.newCachedThreadPool();
//		Task task = new Task();
//		Future<Integer> result = executor.submit(task);
//		
//		executor.shutdown();
//		try {
//				Thread.sleep(1000);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("taks result:" + result.get());
		int ix = 12;
		System.out.println( ix <<=2);
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		Map<String, Object> tmpMap = new HashMap<>();
		tmpMap.put("tvalue", "123");
		tmpMap.put("xvalue", "345");
		tmpMap.put("avalue", "456");
		resultList.add(tmpMap);
		
			
		resultList.forEach(k->{
			Map<String, Object> pMap = k;
			pMap.forEach((t,v)->{
				System.out.println(t);
				System.out.println(v);
			});
		});
	}
}
