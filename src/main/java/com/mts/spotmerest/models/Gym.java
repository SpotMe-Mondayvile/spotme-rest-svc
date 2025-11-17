package com.mts.spotmerest.models;

import jakarta.persistence.*;

//Unlike the other Api's this will not make calls from the database but instead from the Google Maps api
@Entity
@Table(name="GYMS")
public class Gym {
    @Id
    @SequenceGenerator(
            name="gym_sequence",
            sequenceName="gym_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "gym_sequence"
    )
    private Long id;
    private String gymId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;

    public Gym(String id, String name, String address, String city, String state, String zip) {
        this.gymId = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public Gym() {
    }

    public Gym(String gid) {
        this.gymId = gid;
    }

    public Long getId() {
        return id;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
