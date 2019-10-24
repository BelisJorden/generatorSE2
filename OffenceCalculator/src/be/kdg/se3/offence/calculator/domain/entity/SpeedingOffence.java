package be.kdg.se3.offence.calculator.domain.entity;

import java.sql.Timestamp;


/**
 *  Entity representing an Offence child class of type SpeedingOffence
 */
public class SpeedingOffence extends Offence {
    private int maxSpeed;
    private int speed;

    public SpeedingOffence() {
    }

    public SpeedingOffence(int speed, int maxSpeed, Timestamp timestamp, String licencePlate, String city, String street) {
        super(timestamp, licencePlate, street, city);
        this.maxSpeed = maxSpeed;
        this.speed = speed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    @Override
    public String toString() {
        return "SpeedingOffence{" +
                "maxSpeed=" + maxSpeed +
                ", speed=" + speed +
                '}' + super.toString();
    }
}
