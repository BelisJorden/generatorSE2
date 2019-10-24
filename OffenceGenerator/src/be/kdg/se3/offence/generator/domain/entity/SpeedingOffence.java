package be.kdg.se3.offence.generator.domain.entity;

import java.sql.Timestamp;

/**
 *  Entity representing an Offence child class of type SpeedingOffence
 */
public class SpeedingOffence extends Offence {
    private int maxSpeed;
    private int speed;

    public SpeedingOffence(Timestamp timestamp, String licencePlate, String street, String city, int maxSpeed, int speed) {
        super(timestamp, licencePlate, street, city);
        this.maxSpeed = maxSpeed;
        this.speed = speed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public String toString() {
        return  "SpeedingOffence{" +
                "maxSpeed=" + maxSpeed +
                ", speed=" + speed +
                '}' + super.toString();
    }
}
