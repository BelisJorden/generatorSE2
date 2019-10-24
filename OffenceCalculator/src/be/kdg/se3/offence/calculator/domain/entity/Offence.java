package be.kdg.se3.offence.calculator.domain.entity;

import java.sql.Timestamp;
/**
 *  Entity representing an offence parent class that is received from an external communication interface
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

    public Offence() {
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
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
