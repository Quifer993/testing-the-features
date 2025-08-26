package ru.zolo.ttl.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.zolo.Main;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DoSomethingAsync {


    @Autowired
    @Qualifier("ttlTaskExecutor")
    Executor executor;

    //todo вынести в другой класс
    public static void mainAsync(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);
        ContextHelper.addRunIdFromContext("123");

        System.out.println("current start thread = " + Thread.currentThread().getId()
                + "\t context = " + ContextHelper.getRunId());

        for (int i = 0; i < 10; i++) {
            run.getBean(DoSomethingAsync.class).completableDo(i);
        }
    }

    @Async
    public void completableDo(Integer kl) {
        System.out.println(String.format("start %s",kl));
        int taskCount = 999;

        ContextHelper.addRunIdFromContext(kl.toString());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        Set<Object> ids = Collections.synchronizedSet(new HashSet<>());

        AtomicInteger atomicInteger = new AtomicInteger();

        for (int i = 1; i <= taskCount; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                ids.add(Thread.currentThread().getId());
                if(!(kl.toString()).equals(ContextHelper.getRunId())) {
                    System.out.println("+++ " + Thread.currentThread().getId() + " != " + ContextHelper.getRunId());
                    atomicInteger.incrementAndGet();
                }
                System.out.println(kl + " : doSomething  ---- current start thread = " + finalI + " " + Thread.currentThread().getId()
                        + "\t context = " + ContextHelper.getRunId());
                ContextHelper.addRunIdFromContext(kl + "_" + Thread.currentThread().getId());

            }, executor);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        System.out.println(atomicInteger.get() + " - error");
    }
}
