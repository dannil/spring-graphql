package com.github.dannil.demo.service;

import com.github.dannil.demo.configuration.KafkaConfiguration;
import com.github.dannil.demo.model.Address;
import com.github.dannil.demo.model.Person;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    @Value(KafkaConfiguration.PERSONS_TOPIC)
    private String topicName;

    private KafkaTemplate<String, Person> kafkaTemplate;

    private Sinks.Many<Person> sink;

    private Flux<Person> flux;

    private Map<String, Person> persons;

    public PersonService(KafkaTemplate<String, Person> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
        flux = sink.asFlux();

        persons = new HashMap<>();
        persons.put("1", new Person("1", "Bob", "Ferguson", new Address("Baker Street 45", "175-42")));
        persons.put("2", new Person("2", "Alice", "Matthews", new Address("Diagonal Alley", "13 BEF-97")));
    }

    public Collection<Person> getPersons() {
        return persons.values();
    }

    public Optional<Person> getPerson(String id) {
        return Optional.ofNullable(persons.get(id));
    }

    public Person addPerson(String id, String firstName, String lastName, Address address) {
        Person person = new Person(id, firstName, lastName, address);
        persons.put(id, person);
        send(person);
        return person;
    }

    public Optional<Person> deletePerson(String id) {
        Optional<Person> person = getPerson(id);
        if (person.isPresent()) {
            persons.remove(id);
            send(person.get());
        }
        return person;
    }

    public Publisher<Person> listen() {
        return flux;
    }

    private void send(Person person) {
        kafkaTemplate.send(topicName, person);
        kafkaTemplate.flush();
    }

    @KafkaListener(topics = KafkaConfiguration.PERSONS_TOPIC)
    private void consumer(Person person) {
        LOGGER.info(person.toString());
        sink.tryEmitNext(person);
    }

}
