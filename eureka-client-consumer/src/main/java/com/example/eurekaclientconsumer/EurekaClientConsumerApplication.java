package com.example.eurekaclientconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.eurekaclientconsumer.filter.UserContext;
import com.example.eurekaclientconsumer.filter.UserContextHolder;
import com.example.eurekaclientconsumer.intercepter.LoggerInterceptor;

@SpringBootApplication
@EnableFeignClients
public class EurekaClientConsumerApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientConsumerApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.getInterceptors().add((request, body, execution) -> {
			HttpHeaders headers = request.getHeaders();
			headers.add(UserContext.CORRELATION_ID,
					UserContextHolder.getContext().getCorrelationId());
			headers.add(UserContext.AUTH_TOKEN,
					UserContextHolder.getContext().getAuthToken());
			return execution.execute(request, body);
		});

		return restTemplate;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor());
	}
}
