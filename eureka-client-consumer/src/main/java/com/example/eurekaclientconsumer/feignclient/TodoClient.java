package com.example.eurekaclientconsumer.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.eurekaclientconsumer.model.Todo;

@FeignClient("eureka-client-todo-service")
public interface TodoClient {
    @GetMapping("/todos")
    public List<Todo> getTodos();

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable int id);
}
