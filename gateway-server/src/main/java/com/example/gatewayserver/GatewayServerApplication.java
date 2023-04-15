package com.example.gatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

import com.example.gatewayserver.filter.FilterUtils;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@Slf4j
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Autowired
	private Tracer tracer;

	@Bean
	public GlobalFilter postGlobalFilter() {
		return (exchange, chain) -> {
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				String traceId = tracer.currentSpan().context().traceIdString();
				log.debug("Adding the correlation id to the outbound headers. {}", traceId);
				exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
				log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
			}));
		};
	}

}
