package be.kdg.se3.offence.calculator.domain;

import be.kdg.se3.offence.calculator.domain.entity.EmissionOffence;
import be.kdg.se3.offence.calculator.domain.entity.Offence;
import be.kdg.se3.offence.calculator.domain.entity.Penalty;
import be.kdg.se3.offence.calculator.domain.entity.SpeedingOffence;
import org.apache.log4j.Logger;

/**
 * Calculator for calculating the price amount for an offence
 */
public class Calculator {
    private Offence offence;
    private Penalty penalty;
    private int amountForLicenseplate;

    private Logger logger = Logger.getLogger(Calculator.class);

    public void setOffence(Offence offence) {
        this.offence = offence;
    }

    public void setPenalty(Penalty penalty) {
        this.penalty = penalty;
    }



    public void setAmountForLicenseplate(int amountForLicenseplate) {
        this.amountForLicenseplate = amountForLicenseplate;
    }


    public int calculate(boolean withHistory) {

        int price = 0;

        if (!withHistory) {
            //calculate withouh history
            logger.info("Now calculating the price without history");
            if (offence instanceof SpeedingOffence) {
                SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                price = calculateSpeedWithoutHistory( speedingOffence);

            } else if (offence instanceof EmissionOffence) {
                price = calculateEmissionWithoutHistory();
            }

        } else {
            // calculate with history
            logger.info("Now calculating the price with history");
            if (offence instanceof SpeedingOffence) {
                SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                price = calculateSpeedWithoutHistory( speedingOffence);
                price = calculateWithHistory(price);

            } else if (offence instanceof EmissionOffence) {
                price = calculateEmissionWithoutHistory();
                price = calculateWithHistory(price);
            }

        }

        return price;
    }


    private int calculateSpeedWithoutHistory(SpeedingOffence speedingOffence) {

        return ((speedingOffence.getSpeed() - speedingOffence.getMaxSpeed()) * penalty.getSpeedFactor());
    }

    private int calculateEmissionWithoutHistory() {
        return penalty.getEmissionFactor();
    }

    private int calculateWithHistory(int priceWithoutHistory) {
        return priceWithoutHistory + (amountForLicenseplate * penalty.getHistoryFactor());

    }

}
