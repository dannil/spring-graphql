//package com.github.dannil.demo.service;
//
//import com.github.dannil.demo.configuration.KafkaConfiguration;
//import com.github.dannil.demo.model.Person;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Sinks;
//import reactor.core.publisher.Sinks.Many;
//import reactor.util.concurrent.Queues;
//
//@Service
//public class PersonReactiveService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(PersonReactiveService.class);
//
//    @Value(KafkaConfiguration.PERSONS_TOPIC)
//    private String topicName;
//
//    private KafkaTemplate<String, Person> kafkaTemplate;
//
//    private Many<Person> sink;
//
//    private Flux<Person> flux;
//
//    public PersonReactiveService(KafkaTemplate<String, Person> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
//        this.flux = sink.asFlux();
//    }
//
//    public Flux<Person> listen() {
//        return flux;
//    }
//
//    public void send(Person person) {
//        kafkaTemplate.send(topicName, person);
//        kafkaTemplate.flush();
//    }
//
//    @KafkaListener(topics = KafkaConfiguration.PERSONS_TOPIC)
//    private void consumer(Person person) {
//        LOGGER.info(person.toString());
//        sink.tryEmitNext(person);
//    }
//
//}
