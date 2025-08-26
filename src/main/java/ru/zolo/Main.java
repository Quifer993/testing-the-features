package ru.zolo;

import com.alibaba.ttl.TtlRunnable;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import ru.zolo.ttl.async.ContextHelper;
import ru.zolo.ttl.async.DoSomethingAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

@SpringBootApplication
public class Main {

    @SneakyThrows
    public static void main(String[] args) {

    }


    public static void gg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("current start thread1 = " + Thread.currentThread().getId() + " ; CONTEXThELP = " + ContextHelper.getRunId());
                IntStream.range(1, 300).boxed().parallel().forEach(it -> {
                    try {
                        if(it>200){
                            Thread.sleep(500);

                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(Thread.currentThread().getId() + "  - " + ContextHelper.getRunId());
                    if ("000".equals(ContextHelper.getRunId())) {
                        System.out.println("THIS");
                    }
                });
            }
        }).start();

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("current start thread2 = " + Thread.currentThread().getId() + " ; CONTEXThELP = " + ContextHelper.getRunId());
                IntStream.range(1, 100).boxed().parallel().forEach(it -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ContextHelper.addRunIdFromContext("000");
                    System.out.println("--- " + Thread.currentThread().getId() + "  - " + ContextHelper.getRunId());
                });
            }
        }).start();
    }

    public static void runParallelStream(List<String> data, Executor bean) {
        bean.execute(TtlRunnable.get(() -> {
            data.parallelStream().forEach(item -> {
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " context: " + ContextHelper.getRunId());
            });
        }));
    }

}