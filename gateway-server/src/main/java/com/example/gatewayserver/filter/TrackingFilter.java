package com.example.gatewayserver.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Order(1)
@Component
@Slf4j
public class TrackingFilter implements GlobalFilter {

	@Autowired
	private Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if (isCorrelationIdPresent(requestHeaders)) {
			log.debug("tmx-correlation-id found in tracking filter: {}. ",
					FilterUtils.getCorrelationId(requestHeaders));
		} else {
			String correlationID = generateCorrelationId();
			exchange = FilterUtils.setCorrelationId(exchange, correlationID);
			log.debug("tmx-correlation-id generated in tracking filter: {}.", correlationID);
		}

		final ServerHttpResponse response = exchange.getResponse();
		final ServerHttpRequest request = exchange.getRequest();
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			String traceId = tracer.currentSpan().context().traceId();
			log.debug("Adding the correlation id to the outbound headers. {}", traceId);
			response.getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
			log.debug("Completing outgoing request for {}.", request.getURI());
		}));
	}

	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		if (FilterUtils.getCorrelationId(requestHeaders) != null) {
			return true;
		} else {
			return false;
		}
	}

	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}

}