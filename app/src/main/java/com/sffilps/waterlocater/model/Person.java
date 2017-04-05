package com.sffilps.waterlocater.model;

/**
<<<<<<< HEAD
 * Created by Carl on 3/6/17.
=======
 * Created by Carl on 3/6/2017.
>>>>>>> master
 */

public class Person {

    public String name;
    public String email;
<<<<<<< HEAD
    public String role;
    public String uID;

    public Person(String name, String email, String role, String uID) {
=======
    public String homeAddress;
    public String role;
    public String uID;

    /**
     * default constructor
     */
    public Person() {

    }

    /**
     * constructor that initializes everything
     * @param name user name
     * @param email user's
     * @param role user's
     * @param uID user's
     * @param homeAddress user's
     */
    public Person(String name, String email, String role, String uID, String homeAddress) {
>>>>>>> master
        this.name = name;
        this.email = email;
        this.role = role;
        this.uID = uID;
<<<<<<< HEAD
    }
=======
        this.homeAddress = homeAddress;
    }

>>>>>>> master
}
