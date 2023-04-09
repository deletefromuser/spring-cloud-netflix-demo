package com.example.eurekaclient;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.eurekaclient.intercepter.LoggerInterceptor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class EurekaClientApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor());
	}

	@Bean
	public Function<String, String> uppercase() {
		return value -> {
			log.info("Function<String, String> uppercase() - Received: " + value);
			return value.toUpperCase();
		};
	}

	@Bean
	public Supplier<LocalDateTime> date() {
		return () -> {
			log.info("Supplier<LocalDateTime> date -> generate message");
			return LocalDateTime.now();
		};
	}

	@Bean
	public Consumer<String> sink() {
		return value -> {
			log.info("Consumer<String> sink() - Received: " + value);
		};
	}

	@Bean
	public Consumer<String> getDate() {
		return value -> {
			log.info("Consumer<String> getDate() - Received: "
					+ new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8));
			// log.info("Consumer<String> getDate() - Received: "
			// + value);
		};
	}

	@PollableBean
	public Supplier<Flux<String>> stringSupplier() {
		return () -> Flux.just("hello", "oh", "bye");
	}

	@Bean
	public ApplicationRunner poller(@Qualifier("my-todo-in-0") PollableMessageSource destIn) {
		return args -> {
			while (true) {
				try {
					if (!destIn.poll(m -> {
						String newPayload = ((String) m.getPayload()).toUpperCase();
						log.info("ApplicationRunner poller(@Qualifier(\"my-todo-in-0\") PollableMessageSource destIn)"
								+ newPayload);
					})) {
						Thread.sleep(15000);
					}
				} catch (Exception e) {
					log.info("ApplicationRunner poller error", e);
				}
			}
		};
	}

}
