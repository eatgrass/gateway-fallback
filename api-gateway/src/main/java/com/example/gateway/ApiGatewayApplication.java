package com.example.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
@RestController
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry) {



        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
        reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
        reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> {
            CircuitBreakerConfig circuitBreakerConfig = circuitBreakerRegistry.getConfiguration(id).orElse(circuitBreakerRegistry.getDefaultConfig());
            TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(100)).cancelRunningFuture(true).build();

                    return new Resilience4JConfigBuilder(id)
                            .circuitBreakerConfig(circuitBreakerConfig)
                            .timeLimiterConfig(timeLimiterConfig)
                            .build();
        });
        return reactiveResilience4JCircuitBreakerFactory;
    }

//    @Bean
//    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(CircuitBreakerRegistry registry) {
//        return factory-> factory.configureDefault(id -> Resilience4JConfigBuilder.);
//    }

    @GetMapping("/localfallback")
    public Mono<ResponseEntity> fallback() {
        return Mono.just(ResponseEntity.ok("local fallback"));
    }

    @Bean
    public GlobalFilter exceptionFilter() {
        return new ExceptionFilter();
    }
}
