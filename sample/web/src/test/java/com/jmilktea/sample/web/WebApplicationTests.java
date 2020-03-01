package com.jmilktea.sample.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
class WebApplicationTests {

    @Test
    public void testMonoPage() {
        executeMonoPage().block();
    }

    public Mono<Boolean> executeMonoPage() {
        AtomicReference<Integer> index = new AtomicReference<>(0);
        Integer size = 10;
        return queryData(index.get(), size).expand(list -> {
            if (CollectionUtils.isEmpty(list)) {
                return Mono.empty();
            }
            return queryData(index.getAndSet(index.get() + 1), size);
        }).flatMap(list -> {
            //执行完queryData后即会执行遍历，然后再执行下一次queryData
            executeMonoPageError(list);
            list.forEach(s -> {
                System.out.println(s);
            });
            return Mono.just(true);
        }).onErrorContinue((t, d) -> {
            System.out.println(t);
        }).collectList().flatMap(s -> Mono.just(true));
    }

    /**
     * 这里可以是数据库，或者调用接口等耗时操作
     *
     * @param index
     * @param size
     * @return
     */
    public Mono<List<Integer>> queryData(Integer index, Integer size) {
        if (index == 10) {
            return Mono.empty();
        }
        List<Integer> list = new ArrayList<>();
        for (Integer i = index * size; i < ((index + 1) * size); i++) {
            list.add(i);
        }
        return Mono.just(list);
    }

    public void executeMonoPageError(List<Integer> list) {
        if (list.get(0).equals(10)) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testDebug() {
        //Hooks.onOperatorDebug();
        //Flux.just(1, 2, 3).single().checkpoint("debug").subscribe();
        //Flux.just(1, 2, 3).single().log("debug").subscribe();
        Flux.just(1, 2, 3, 4)
                .flatMap(s -> {
                    System.out.println("item:" + s);
                    return Mono.just(s * 2);
                })
                .filter(s -> s % 2 != 0)
                .single()
                .log("single debug")
                .doOnNext(s -> System.out.println("single result is:" + s))
                .subscribe();
    }
}
