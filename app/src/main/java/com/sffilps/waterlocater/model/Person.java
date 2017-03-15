package com.sffilps.waterlocater.model;

/**
 * Created by Carl on 3/6/2017.
 */

public class Person {

    public String name;
    public String email;
    public String homeAddress;
    public String role;
    public String uID;

    public Person() {

    }

    public Person(String name, String email, String role, String uID, String homeAddress) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.uID = uID;
        this.homeAddress = homeAddress;
    }

}
