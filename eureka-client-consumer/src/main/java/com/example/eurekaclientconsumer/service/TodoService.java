package com.example.eurekaclientconsumer.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaclientconsumer.feignclient.TodoClient;
import com.example.eurekaclientconsumer.filter.UserContextHolder;
import com.example.eurekaclientconsumer.model.Todo;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TodoService {

    RestTemplate restTemplate;

    TodoClient todoClient;

    public TodoService(@LoadBalanced RestTemplate restTemplate, TodoClient todoClient) {
        this.restTemplate = restTemplate;
        this.todoClient = todoClient;
    }

    // use @LoadBalanced RestTemplate
    public List<Todo> getTodosByRestTemplate() {
        return restTemplate.exchange("http://eureka-client-todo-service/todos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Todo>>() {
                }).getBody();

    }

    // use FeignClient
    public List<Todo> getTodos() {
        return todoClient.getTodos();
    }

    // use FeignClient
    @CircuitBreaker(name = "getTodoWithId", fallbackMethod = "getTodoFallback")
    @Bulkhead(name = "getTodoWithIdBulkhead", fallbackMethod = "getTodoBulkFallback")
    @Retry(name = "retryTodoService", fallbackMethod = "getTodoRetryFallback")
    public Todo getTodo(int id) throws TimeoutException {
        log.info("getTodo() get called");
        if (id == 111) {
            sleep();
        }
        randomlyRunLong();
        return todoClient.getTodo(id);
    }

    @Bulkhead(name = "getTodoWithIdBulkhead", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "getTodoBulkFallback")
    public Todo getTodoBulkHead(int id) throws TimeoutException, InterruptedException {
        log.info("getTodoBulkHead() get called");
        if (id == 111) {
            Thread.sleep(5000);
        }
        return todoClient.getTodo(id);
    }

    @Retry(name = "retryTodoService", fallbackMethod = "getTodoRetryFallback")
    public Todo getTodoRetry(int id) throws TimeoutException {
        log.info("getTodoRetry() get called");
        if (id == 111) {
            sleep();
        }
        randomlyRunLong();
        return todoClient.getTodo(id);
    }

    @RateLimiter(name = "ratelimiterTodoService", fallbackMethod = "getTodoLimiterFallback")
    public Todo getTodoRateLimiter(int id) throws TimeoutException, InterruptedException {
        log.info("getTodoRateLimiter() get called");
        log.info("TodoController getTodoRateLimiter Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());

        if (id == 111) {
            Thread.sleep(5000);
        }
        return todoClient.getTodo(id);
    }

    public Todo getTodoFallback(int id, Throwable t) throws TimeoutException {
        Todo dummy = new Todo(0, 0, "not available", false);
        return dummy;
    }

    public Todo getTodoBulkFallback(int id, Throwable t) throws TimeoutException {
        Todo dummy = new Todo(0, 0, "not available in bulk", false);
        return dummy;
    }

    public Todo getTodoRetryFallback(int id, Throwable t) throws TimeoutException {
        Todo dummy = new Todo(0, 0, "not available in retry", false);
        return dummy;
    }

    public Todo getTodoLimiterFallback(int id, Throwable t) throws TimeoutException {
        log.info("getTodoLimiterFallback ", t);
        Todo dummy = new Todo(0, 0, "not available in rate limiter", false);
        return dummy;
    }

    // use Config Server refresh
    @GetMapping("/todos/length")
    public Map<String, Integer> configRefresh(@Value("${todo.list.length}") int length) {
        return Map.of("length", length);
    }

    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3)
            sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
