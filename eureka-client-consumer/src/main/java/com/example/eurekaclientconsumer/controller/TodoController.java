package com.example.eurekaclientconsumer.controller;

import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaclientconsumer.model.Todo;

@RestController
public class TodoController {

    RestTemplate restTemplate;

    public TodoController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // use @LoadBalanced RestTemplate
    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return restTemplate.exchange("http://eureka-client-todo-service/todos", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Todo>>() {
                }).getBody();

    }
}
