package com.example.eurekaclientconsumer.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaclientconsumer.model.Todo;
import com.example.eurekaclientconsumer.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TodoController {

    TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping("/todos_c")
    public List<Todo> getTodosByRestTemplate() {
        return service.getTodosByRestTemplate();

    }

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("----- spring security role start-----");
        auth.getAuthorities().stream().forEach(item -> log.info(item.getAuthority()));
        log.info("----- spring security role end-----");
        return service.getTodos();
    }

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable int id) throws TimeoutException {
        return service.getTodo(id);
    }

    @GetMapping("/todos/b/{id}")
    public Todo getTodoBulkHead(@PathVariable int id) throws TimeoutException, InterruptedException {
        return service.getTodoBulkHead(id);
    }

    @GetMapping("/todos/r/{id}")
    public Todo getTodoRetry(@PathVariable int id) throws TimeoutException {
        return service.getTodoRetry(id);
    }

    @GetMapping("/todos/l/{id}")
    public Todo getTodoRateLimiter(@PathVariable int id) throws TimeoutException, InterruptedException {
        return service.getTodoRateLimiter(id);
    }

    // use Config Server refresh
    @GetMapping("/todos/length")
    public Map<String, Integer> configRefresh(@Value("${todo.list.length}") int length) {
        return Map.of("length", length);
    }

}
