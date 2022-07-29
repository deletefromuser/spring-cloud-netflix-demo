package com.example.eurekaclientconsumer.controller;

import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaclientconsumer.feignclient.TodoClient;
import com.example.eurekaclientconsumer.model.Todo;

@RestController
public class TodoController {

    RestTemplate restTemplate;

    TodoClient todoClient;

    public TodoController(@LoadBalanced RestTemplate restTemplate, TodoClient todoClient) {
        this.restTemplate = restTemplate;
        this.todoClient = todoClient;
    }

    // use @LoadBalanced RestTemplate
    @GetMapping("/todos_c")
    public List<Todo> getTodosByRestTemplate() {
        return restTemplate.exchange("http://eureka-client-todo-service/todos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Todo>>() {
                }).getBody();

    }

    // use FeignClient
    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todoClient.getTodos();

    }

    // use FeignClient
    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable int id) {
        return todoClient.getTodo(id);

    }
}
