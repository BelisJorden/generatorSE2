package be.kdg.se3.offence.calculator.domain.entity;

/**
 *  Entity wich contains the penalty values we receive from the PenaltyService
 *  These values are used for calculating the offence amount
 */
public class Penalty {
    private int speedFactor;
    private int emissionFactor;
    private int historyFactor;




    public int getSpeedFactor() {
        return speedFactor;
    }

    public int getEmissionFactor() {
        return emissionFactor;
    }

    public int getHistoryFactor() {
        return historyFactor;
    }

    public void setSpeedFactor(int speedFactor) {
        this.speedFactor = speedFactor;
    }

    public void setEmissionFactor(int emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public void setHistoryFactor(int historyFactor) {
        this.historyFactor = historyFactor;
    }
}
