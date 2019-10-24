package be.kdg.se3.offence.generator.domain.entity;

import java.sql.Timestamp;

/**
 *  Entity representing an offence parent class
 */
public class Offence {
    private Timestamp timestamp;
    private String licencePlate;
    private String street;
    private String city;

    public Offence(Timestamp timestamp, String licencePlate, String street, String city) {
        this.timestamp = timestamp;
        this.licencePlate = licencePlate;
        this.street = street;
        this.city = city;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Offence{" +
                "timestamp=" + timestamp +
                ", licencePlate='" + licencePlate + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}