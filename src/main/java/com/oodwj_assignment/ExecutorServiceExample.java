package com.oodwj_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {
    private static final Random PRNG = new Random();

    private static class Result {
        private final int wait;

        public Result(int code) {
            this.wait = code;
        }
    }

    public static Result compute(Object obj) throws InterruptedException {
        int wait = PRNG.nextInt(3000);
        Thread.sleep(wait);
        return new Result(wait);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Object> objects = new ArrayList<Object>();
        for (int i = 0; i < 100; i++) {
            objects.add(new Object());
        }

        List<Callable<Result>> tasks = new ArrayList<Callable<Result>>();
        for (final Object object : objects) {
            Callable<Result> c = new Callable<Result>() {
                @Override
                public Result call() throws Exception {
                    return compute(object);
                }
            };
            tasks.add(c);
        }

        ExecutorService exec = Executors.newCachedThreadPool();
        // some other exectuors you could try to see the different behaviours
        // ExecutorService exec = Executors.newFixedThreadPool(3);
        // ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            long start = System.currentTimeMillis();
            List<Future<Result>> results = exec.invokeAll(tasks);
            int sum = 0;
            for (Future<Result> fr : results) {
                sum += fr.get().wait;
                System.out.println(String.format("Task waited %d ms",
                        fr.get().wait));
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(String.format("Elapsed time: %d ms", elapsed));
            System.out.println(String.format("... but compute tasks waited for total of %d ms; speed-up of %.2fx", sum, sum / (elapsed * 1d)));
        } finally {
            exec.shutdown();
        }
    }
}