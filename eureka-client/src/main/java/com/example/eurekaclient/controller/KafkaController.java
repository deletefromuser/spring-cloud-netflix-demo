package com.example.eurekaclient.controller;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaclient.model.Todo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class KafkaController {

    @Autowired
    private Function<String, String> uppercase;

    @Autowired
    private Supplier<LocalDateTime> date;

    @RequestMapping("/createKafkaMessage")
    public String createKafkaMessage(@RequestParam(required = false) String msg) {
        uppercase.apply(StringUtils.defaultIfBlank(msg, "createKafkaMessage"));
        date.get();

        return "Hello World!";
    }

    @RequestMapping("/receiveKafkaMessage")
    public String receiveKafkaMessage() {

        return "Hello World!";
    }

    @Autowired
    private PollableMessageSource source;

    @Scheduled(fixedDelay = 5_000)
    public void poll() {
        log.info("Polling...");
        this.source.poll(m -> {
            log.info(m.getPayload().toString());
        }, new ParameterizedTypeReference<Todo>() {
        });
    }
}
