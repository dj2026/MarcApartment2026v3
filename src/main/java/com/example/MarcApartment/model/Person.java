package com.example.MarcApartment.model;
import jakarta.persistence.*;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; private String name; private String email;
    public Person() {}
    public Person(String name, String email) {this.name = name;this.email = email;}

    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getName() {return name;} public void setName(String name) {this.name = name;}
    public String getEmail() {return email;} public void setEmail(String email) {this.email = email;}
}