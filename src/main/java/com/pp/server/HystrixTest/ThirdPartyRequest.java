package com.pp.server.HystrixTest;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThirdPartyRequest extends HystrixCommand<String> {

    private String name;

    public ThirdPartyRequest(String name)
    {
        super(HystrixCommandGroupKey.Factory.asKey(name));
        this.name = name;
    }

    protected  String run()
    {
        System.out.println("------run--------");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "run ok";
    }

    public static void main(String args[]) throws ExecutionException, InterruptedException, TimeoutException {
        ThirdPartyRequest thirdPartyRequest = new ThirdPartyRequest("third");
        /***************************
         * execute 同步等待
         */
        //String result = thirdPartyRequest.execute();
//        Future<String> squeue= thirdPartyRequest.queue();
//        /**
//         * 异步工作
//         */
//        String queueResult = squeue.get(20000, TimeUnit.MILLISECONDS);
        /**
         *
         */
//        System.out.println(queueResult);

        Observable<String>  observable = thirdPartyRequest.observe();
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                    System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onNext(String s) {
               System.out.println("OnNext:"+ s);
            }
        });
        Thread.sleep(100000);
    }
}
