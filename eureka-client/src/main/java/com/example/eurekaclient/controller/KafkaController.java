package com.example.eurekaclient.controller;

import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KafkaController {

    @Autowired
    private Function<String, String> uppercase;

    @RequestMapping("/createKafkaMessage")
    public String createKafkaMessage(@RequestParam(required = false) String msg) {
        uppercase.apply(StringUtils.defaultIfBlank(msg, "createKafkaMessage"));

        return "Hello World!";
    }

    @RequestMapping("/receiveKafkaMessage")
    public String receiveKafkaMessage() {

        return "Hello World!";
    }
}
