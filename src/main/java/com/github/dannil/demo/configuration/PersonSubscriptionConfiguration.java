package com.github.dannil.demo.configuration;

import com.github.dannil.demo.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Configuration
public class PersonSubscriptionConfiguration {

    @Bean
    public Sinks.Many<Person> personSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<Person> personFlux(Sinks.Many<Person> personSink) {
        return personSink.asFlux();
    }

}
