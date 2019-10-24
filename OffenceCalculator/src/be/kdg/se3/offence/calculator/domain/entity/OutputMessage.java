package be.kdg.se3.offence.calculator.domain.entity;

/**
 *  Entity wich represents offence and calculated offence amount
 *  This will be send to an external communication interface as the output
 */
public class OutputMessage {
    private Offence offence;
    private int offenceAmount;


    public OutputMessage(Offence offence, int offenceAmount) {
        this.offence = offence;
        this.offenceAmount = offenceAmount;
    }

    public Offence getOffence() {
        return offence;
    }

    public int getOffenceAmount() {return offenceAmount;}
}
