package com.github.dannil.demo.service;

import com.github.dannil.demo.model.Address;
import com.github.dannil.demo.model.Person;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private int nextId;

    private Map<Integer, Address> addresses;

    public AddressService() {
        nextId = 1;
        addresses = new HashMap<>();
        addresses.put(nextId++, new Address("Baker Street 45", "175-42", "1"));
        addresses.put(nextId++, new Address("Downing Street 92", "99-11-421", "1"));
        addresses.put(nextId++, new Address("Diagonal Alley", "13 BEF-97", "2"));
        addresses.put(nextId++, new Address("Triangle Way", "95 LL 1237", "3"));
        addresses.put(nextId++, new Address("Octagon Boulevard", "89 PW-42", "3"));
        nextId++;
    }

    public Collection<Address> getAddresses(Person person) {
        return addresses.values().stream()
                .filter(a -> a.getPersonId().equals(person.getId()))
                .toList();
    }

    public Map<Person, Collection<Address>> getAddresses(Collection<Person> persons) {
        return persons.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Function.identity(),
                        person -> addresses.values().stream()
                                .filter(a -> a.getPersonId().equals(person.getId()))
                                .toList()
                )
        );
    }

    public Address addAddress(String street, String zipCode, String personId) {
        Address address = new Address(street, zipCode, personId);
        addresses.put(nextId++, address);
        return address;
    }

}
