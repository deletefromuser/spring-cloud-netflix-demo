package com.example.eurekaclient.controller;

import java.util.List;

import org.springframework.cloud.sleuth.ScopedSpan;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.eurekaclient.model.Todo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TodoController {

    RestTemplate restTemplate;

    Tracer tracer;

    public TodoController(RestTemplate restTemplate, Tracer tracer) {
        this.restTemplate = restTemplate;
        this.tracer = tracer;
    }

    public TodoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private List<Todo> list;

    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return getTodoList();

    }

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable int id) {
        return getTodoList().get(id);
    }

    @GetMapping("/closed")
    public String closed() {
        return "closed";
    }

    private List<Todo> getTodoList() {
        log.info("service called");
        if (list == null) {
            ScopedSpan span = tracer.startScopedSpan("jsonplaceholder");
            list = restTemplate.exchange("https://jsonplaceholder.typicode.com/todos", HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Todo>>() {
                    }).getBody();
            span.tag("third_party", "jsonplaceholder");
            span.end();
        }
        return list;
    }
}
