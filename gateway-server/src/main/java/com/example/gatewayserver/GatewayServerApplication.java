package com.example.gatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;

import com.example.gatewayserver.filter.FilterUtils;

import lombok.extern.slf4j.Slf4j;

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
			{
				Span currentSpan = tracer.currentSpan();
				if (currentSpan != null) {
					String traceId = tracer.currentSpan().context().traceId();
					log.debug("Adding the correlation id to the outbound headers. {}", traceId);
					exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
					log.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
				}

				return chain.filter(exchange);
			}
		};
	}

}
