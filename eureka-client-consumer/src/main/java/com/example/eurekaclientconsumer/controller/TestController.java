package com.example.eurekaclientconsumer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @GetMapping("/decrypt")
    public Map<String, String> decrypt(@Value("${cc.asako.url}") String url) {
        log.info(url);
        return Map.of("url", url);
    }

}
