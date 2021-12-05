package com.exceptionresolve.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestDemo {


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());


        List<MyTask> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new MyTask());
        }

        List<Future<String>> futures = threadPoolExecutor.invokeAll(list);

        for (Future<String> future : futures) {
            System.out.println(future.get());
        }
        threadPoolExecutor.shutdown();

//        for (MyTask myTask : list) {
//            Future<String> submit = threadPoolExecutor.submit(myTask);
//            futures.add(submit);
//        }

//        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(threadPoolExecutor);
//        for (MyTask myTask : list) {
//            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");
//            executorCompletionService.submit(myTask);
//        }

//        for (int i = 0; i < 10; i++) {
//            System.out.println(executorCompletionService.take().get());
//        }
    }
}
