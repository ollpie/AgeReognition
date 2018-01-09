package com.example.olive.agerecognitionstudy;

import java.util.UUID;

/**
 * Created by olive on 04.01.2018.
 */

public class Participant {

    String id;
    int age;
    String gender;

    // constructors
    public Participant() {
    }

    public Participant(int age, String gender) {
        this.id = UUID.randomUUID().toString();
        this.age = age;
        this.gender = gender;
    }

    public Participant(String id, int age, String gender) {
        this.id = id;
        this.age = age;
        this.gender = gender;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // getters
    public String getId() {
        return this.id;
    }

    public int getAge() {
        return this.age;
    }

    public String getGender() {
        return this.gender;
    }
}
