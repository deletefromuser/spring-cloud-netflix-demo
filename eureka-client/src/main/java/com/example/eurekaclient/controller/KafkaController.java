package com.example.eurekaclient.controller;

import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaclient.model.Todo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KafkaController {

    @Autowired
    private Processor pipe;

    @Autowired
    private Function<String, String> uppercase;

    @RequestMapping("/createKafkaMessage")
    public String createKafkaMessage(@RequestParam(required = false) String msg) {
        pipe.input()
                .send(MessageBuilder.withPayload(new Todo("This is my message"))
                        .build());

        uppercase.apply(StringUtils.defaultIfBlank(msg, "createKafkaMessage"));

        return "Hello World!";
    }

    @RequestMapping("/receiveKafkaMessage")
    public String receiveKafkaMessage() {

        return "Hello World!";
    }
}
