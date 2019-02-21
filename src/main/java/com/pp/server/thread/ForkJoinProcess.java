package com.pp.server.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;




public class ForkJoinProcess extends RecursiveTask<List<String>>{
	
	private final String path;
	
	private final String extension;

	public ForkJoinProcess(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}
	
	@Override
	protected List<String> compute() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		List<ForkJoinProcess> tasks = new ArrayList<>();
		File file = new File(path);
		File[] contends = file.listFiles();
		if (contends != null) {
			for(File fContent: contends)
			{
				if (fContent.isDirectory()) {
					ForkJoinProcess task = new ForkJoinProcess(fContent.getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				}else {
					if (checkfile(fContent.getName())) {
						list.add(fContent.getAbsolutePath());
					}
				}
			}
		}
		if (tasks.size()> 50) {
			System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), tasks.size());
		}
		addResultToTasks(list, tasks);
		return list;
	}
	
	public void addResultToTasks(List<String> list, List<ForkJoinProcess> tasks)
	{
		for(ForkJoinProcess task: tasks)
		{
			list.addAll(task.join());
		}
	}
	
	
	public boolean checkfile(String name) {
		return name.endsWith(extension);
	}

	public static void main(String[] args) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("superTV", 0);
		resultMap.put("superTV", 1);
		System.out.println(resultMap.get("superTV"));
//		ForkJoinPool poll = new ForkJoinPool();
//		ForkJoinProcess system = new ForkJoinProcess("D:\\data", "json");
//		ForkJoinProcess library = new ForkJoinProcess("D:\\demo", "html");
//		ForkJoinProcess user = new ForkJoinProcess("D:\\e", "log");
//		
//		poll.execute(system);
//		poll.execute(library);
//		poll.execute(user);
//		do
//        {
//            System.out.printf("******************************************\n");
//            System.out.printf("Main: Parallelism: %d\n", poll.getParallelism());
//            System.out.printf("Main: Active Threads: %d\n", poll.getActiveThreadCount());
//            System.out.printf("Main: Task Count: %d\n", poll.getQueuedTaskCount());
//            System.out.printf("Main: Steal Count: %d\n", poll.getStealCount());
//            System.out.printf("******************************************\n");
//            try
//            {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        } while ((!system.isDone()) || (!library.isDone()) || (!user.isDone()));
//		poll.shutdown();
//        List<String> results;
//        results = system.join();
//        System.out.printf("System: %d files found.\n", results.size());
//        results = library.join();
//        System.out.printf("Library: %d files found.\n", results.size());
//        results = user.join();
//        System.out.printf("Users: %d files found.\n", results.size());
	}
}
