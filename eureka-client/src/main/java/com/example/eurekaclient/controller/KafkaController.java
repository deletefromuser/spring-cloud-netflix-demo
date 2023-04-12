package com.example.eurekaclient.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang.ObjectUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaclient.model.Todo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class KafkaController {

    @Autowired
    private Function<String, String> uppercase;

    @Autowired
    private Supplier<LocalDateTime> date;

    // @Autowired
    // @Qualifier("uppercase-out-0")
    // private MessageChannel uppercaseOut;
    @Autowired(required = false)
    @Qualifier("barin")
    private MessageChannel uppercaseOut;
    @Autowired(required = false)
    @Qualifier("barin-in-0")
    private MessageChannel uppercaseOut2;
    @Autowired(required = false)
    @Qualifier("barin-out-0")
    private MessageChannel uppercaseOut3;

    // @Autowired
    // KafkaMessageChannelBinder kafkaMessageChannelBinder;

    @Autowired
    private ProducerFactory<String, String> producer;

    @RequestMapping("/createKafkaMessage")
    public String createKafkaMessage(@RequestParam(required = false) String msg) {
        // uppercase.apply(StringUtils.defaultIfBlank(msg, "createKafkaMessage"));
        // date.get();

        MessageChannel channel = (MessageChannel) ObjectUtils.defaultIfNull(uppercaseOut, uppercaseOut2);
        if (channel != null) {
            log.info("channel.send");
            channel.send(org.springframework.integration.support.MessageBuilder
                    .withPayload(msg).build());
        }
        if (uppercaseOut3 != null) {
            log.info("uppercaseOut3.send");
            uppercaseOut3.send(org.springframework.integration.support.MessageBuilder
                    .withPayload(msg).build());
        }

        try {
            producer.updateConfigs(Map.of("key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                    "value.serializer", "org.apache.kafka.common.serialization.StringSerializer"));
            producer.createProducer().send(new ProducerRecord<>("sink-in-0", msg));
        } catch (Throwable ex) {
            log.error("", ex);
        }

        log.info("createKafkaMessage");

        return "Hello World!";
    }

    // @Input
    // PollableMessageSource orders();

    @RequestMapping("/receiveKafkaMessage")
    public String receiveKafkaMessage() {

        return "Hello World!";
    }

    @Autowired
    Supplier<Flux<String>> stringSupplier;

    @RequestMapping("/pollable")
    // @Qualifier("stringSupplier")
    public String pollableSupplier() {
        stringSupplier.get();
        return "pollableSupplier execute sucessfully";
    }

    @Autowired
    @Qualifier("blah-in-0")
    private PollableMessageSource source;
    @Autowired
    @Qualifier("my-todo-in-0")
    private PollableMessageSource source2;

    @Scheduled(fixedDelay = 5_000)
    public void poll() {
        try {
            log.info("Polling topic [my-todo-in-0]...");
            this.source2.poll(m -> {
                log.info("---" + m.getPayload().toString() + "---");
            }, new ParameterizedTypeReference<Todo>() {
            });

            log.info("Polling topic [blah-in-0]...");
            source.poll(m -> {
                log.info("***" + m.getPayload().toString() + "***");
            });
        } catch (Throwable ex) {
            log.error("", ex);
        }
    }
}
