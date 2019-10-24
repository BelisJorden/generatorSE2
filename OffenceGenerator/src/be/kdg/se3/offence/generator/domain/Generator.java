package be.kdg.se3.offence.generator.domain;

import be.kdg.se3.offence.generator.domain.entity.EmissionOffence;
import be.kdg.se3.offence.generator.domain.entity.Offence;
import be.kdg.se3.offence.generator.domain.entity.SpeedingOffence;
import be.kdg.se3.offence.generator.domain.service.GeneratorService;


import java.sql.Timestamp;
import java.util.Random;

/**
 * Generates offences using adjustable in memory values
 */
public class Generator implements GeneratorService {

    private int speedOffencePercentage;


    private int[] maxSpeedList;
    private String[] cityList;
    private String[] licensePlateList;
    private int speedUpperLimit;


    public Generator(int speedOffencePercentage, int[] maxSpeedList, String[] cityList, String[] licensePlateList,int speedUpperLimit) {
        this.speedOffencePercentage = speedOffencePercentage;
        this.maxSpeedList = maxSpeedList;
        this.cityList = cityList;
        this.licensePlateList = licensePlateList;
        this.speedUpperLimit = speedUpperLimit;
    }

    /**
     * Generates and returns a type of offence
     */
    @Override
    public Offence generate() {
        Timestamp timestamp = generateTimestamp();
        String licenseplate = generateLicenseplate(licensePlateList);
        String street =  generateStreet();
        String city = generateCity(cityList);


        Offence offence;
        //Determine which type of offence will be generated, depending on the adjustable speedOffencePercentage
        if (Math.random() * 100 < speedOffencePercentage) {
            int maxSpeed = generateMaxSpeed(maxSpeedList);
            int speed = generateSpeed(maxSpeed);
            offence = new SpeedingOffence(timestamp, licenseplate, street, city, maxSpeed, speed);
        }
        else {
            int mineuronorm = generateMinEuronorm();
            int euronorm =  generateEuronorm(mineuronorm);

            offence = new EmissionOffence(timestamp,licenseplate,street,city,mineuronorm,euronorm);
        }


        return offence;
    }

    private int generateSpeed(int maxSpeed) {
        Random r = new Random();
        int min = maxSpeed + 1;
        int max = speedUpperLimit;
        return r.nextInt(max-min) + min;

    }

    private String generateCity(String[] cityList) {
        int random = new Random().nextInt(cityList.length);
        return cityList[random];
    }

    private String generateLicenseplate(String[] licensePlateList) {
        int random = new Random().nextInt(licensePlateList.length);
        return licensePlateList[random];
    }

    private int generateMaxSpeed(int[] maxSpeedList) {
        int random = new Random().nextInt(maxSpeedList.length);
        return maxSpeedList[random];
    }


    private String generateStreet() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 100; i++) {
            char c = chars[r.nextInt(chars.length)];
            sb.append(c);
        }
        return  sb.toString();
    }

    private int generateMinEuronorm() {
        Random r = new Random();
        int min = 3;
        int max = 7;
        int result = r.nextInt(max-min) + min;
        return result;
    }

    private int generateEuronorm(int mineuronorm) {
        Random r = new Random();
        int min = 1;
        int max = mineuronorm;
        int result = r.nextInt(max-min) + min;
        return  result;
    }

    private Timestamp generateTimestamp() {
        long offset = Timestamp.valueOf("2017-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2018-01-01 00:00:00").getTime();
        long diff = end - offset + 1;
        Timestamp timestamp = new Timestamp(offset + (long)(Math.random() * diff));
        return  timestamp;
    }



}
