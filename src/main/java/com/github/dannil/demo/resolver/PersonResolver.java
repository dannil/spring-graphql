package com.github.dannil.demo.resolver;

import com.github.dannil.demo.model.Address;
import com.github.dannil.demo.model.Person;
import com.github.dannil.demo.service.AddressService;
import com.github.dannil.demo.service.PersonService;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PersonResolver {

    private AddressService addressService;

    private PersonService personService;

    @QueryMapping
    public Collection<Person> persons(@Argument Optional<String> id) {
        if (id.isPresent()) {
            return List.of(personService.getPerson(id.get()));
        }
        return personService.getPersons();
    }

    @BatchMapping
    public Map<Person, Collection<Address>> addresses(Collection<Person> persons) {
        return addressService.getAddresses(persons);
    }

    @MutationMapping
    public Person addPerson(@Argument String id, @Argument String firstName, @Argument String lastName, @Argument Collection<Address> addresses) {
        for (Address address : addresses) {
            addressService.addAddress(address.getStreet(), address.getZipCode(), id);
        }
        return personService.addPerson(id, firstName, lastName);
    }

    @MutationMapping
    public Optional<Person> deletePerson(@Argument String id) {
        return personService.deletePerson(id);
    }

    @SubscriptionMapping
    public Publisher<Person> personSubscription() {
        return personService.notifyChange();
    }

}
