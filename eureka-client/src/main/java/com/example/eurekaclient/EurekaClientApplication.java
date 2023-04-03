package com.example.eurekaclient;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.eurekaclient.intercepter.LoggerInterceptor;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
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
		return log::info;
	}
}
