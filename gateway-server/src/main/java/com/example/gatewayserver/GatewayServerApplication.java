package com.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

import com.example.gatewayserver.filter.FilterUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public GlobalFilter postGlobalFilter() {
		return (exchange, chain) -> {
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
				String correlationId = FilterUtils.getCorrelationId(requestHeaders);
				log.debug("Adding the correlation id to the outbound headers. {}", correlationId);
				exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
				log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
			}));
		};
	}

}
