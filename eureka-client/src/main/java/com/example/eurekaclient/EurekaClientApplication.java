package com.example.eurekaclient;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.eurekaclient.intercepter.LoggerInterceptor;

import lombok.extern.slf4j.Slf4j;

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
			log.info("Received: " + value);
			return value.toUpperCase();
		};
	}

	@Bean
	public Supplier<LocalDateTime> date() {
		return () -> LocalDateTime.now();
	}

	@Bean
	public Consumer<String> sink() {
		log.info("Consumer<String> sink() called");
		return log::info;
	}

	@Bean
	public ApplicationRunner poller(PollableMessageSource destIn) {
		return args -> {
			while (true) {
				try {
					if (!destIn.poll(m -> {
						String newPayload = ((String) m.getPayload()).toUpperCase();
						log.info(newPayload);
					})) {
						Thread.sleep(15000);
					}
				} catch (Exception e) {
					log.info("", e);
				}
			}
		};
	}

}
