package com.github.dannil.demo.model;

import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Person {

    private String id;
    private String firstName;
    private String lastName;
    private Address address;

}
