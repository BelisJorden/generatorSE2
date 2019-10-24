package be.kdg.se3.offence.calculator.domain;

import be.kdg.se3.offence.calculator.domain.entity.EmissionOffence;
import be.kdg.se3.offence.calculator.domain.entity.Offence;
import be.kdg.se3.offence.calculator.domain.entity.OutputMessage;
import be.kdg.se3.offence.calculator.domain.entity.SavedEmissionOffences;
import be.kdg.se3.offence.calculator.domain.service.InputListener;
import be.kdg.se3.offence.calculator.domain.service.InputService;
import be.kdg.se3.offence.calculator.domain.service.OutputService;
import be.kdg.se3.offence.calculator.service.PenaltyService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;


public class Controller implements InputListener {
    private Logger logger = Logger.getLogger(Controller.class);

    private InputService inputService;
    private PenaltyService penaltyService;

    private boolean calculateWithHistory;
    private Calculator calculator;
    private SavedEmissionOffences savedEmissionOffences;
    private OutputService outputService;


    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }

    public void setEmissionOffences(SavedEmissionOffences savedEmissionOffences) {
        this.savedEmissionOffences = savedEmissionOffences;
    }

    public PenaltyService getPenaltyService() {
        return penaltyService;
    }

    public Calculator getCalculator() {
        return calculator;
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public void setPenaltyService(PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }


    public boolean isCalculateWithHistory() {
        return calculateWithHistory;
    }

    public void setCalculateWithHistory(boolean calculateWithHistory) {
        this.calculateWithHistory = calculateWithHistory;
    }


    public InputService getInputService() {
        return inputService;
    }

    public void setInputService(InputService inputService) {
        this.inputService = inputService;
    }

    public void start() {
        try {
            inputService.initialize(this);
            outputService.initialize();
        } catch (CommunicationException e) {
            logger.fatal("Unable to initialize communication channel", e);
        }
    }

    public void stop() {
        try {
            inputService.shutdown();
            outputService.shutdown();
        } catch (CommunicationException e) {
            logger.error("Unable to properly shut down communication channel");
        }
    }


    @Override
    public void onReceive(Offence offence) {
        int price = 0;
        boolean ignoreEmissionOffence = false;
        boolean licencePlateError = false;
        savedEmissionOffences.clearSavedEmissionOffencesIfExpired();
        if (offence instanceof EmissionOffence) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ignoreEmissionOffence = savedEmissionOffences.checkSavedEmissionOffences((EmissionOffence) offence);
            savedEmissionOffences.addEmissionOffence((EmissionOffence) offence, timestamp);

        }
        if (!ignoreEmissionOffence) {
            calculator.setPenalty(penaltyService.getPenalty());
            calculator.setOffence(offence);
            if (calculateWithHistory) {
                int amountForLicenseplate = penaltyService.getAmountForLicensceplate(offence.getLicencePlate());
                if (amountForLicenseplate == 0) {
                    licencePlateError = true;
                }
                if (!licencePlateError) {
                    calculator.setAmountForLicenseplate(amountForLicenseplate);
                }

            }
            if (!licencePlateError) {
                //calculate if no errors
                price = calculator.calculate(calculateWithHistory);
                logger.info("The calculated price is " + price);
            }

        }
        if (ignoreEmissionOffence) {
            logger.info("This offence will be ignored because it already occured in this zone some time ago");
        }

        if (!ignoreEmissionOffence && !licencePlateError) {
            OutputMessage outputMessage = new OutputMessage(offence, price);
            try {
                outputService.publish(outputMessage);
            } catch (IOException e) {
                logger.error("Error while publishing the message to the output service");
            }
        }
    }
}
