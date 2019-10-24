package be.kdg.se3.offence.calculator;

import be.kdg.se3.offence.calculator.adapters.input.RabbitMQInput;
import be.kdg.se3.offence.calculator.adapters.output.RabbitMQOutput;
import be.kdg.se3.offence.calculator.domain.Calculator;
import be.kdg.se3.offence.calculator.domain.Controller;
import be.kdg.se3.offence.calculator.domain.entity.SavedEmissionOffences;

import be.kdg.se3.offence.calculator.domain.service.InputService;
import be.kdg.se3.offence.calculator.domain.service.OutputService;
import be.kdg.se3.offence.calculator.service.PenaltyService;

import java.util.Timer;
import java.util.TimerTask;


public class Main {
    public static void main(String[] args) {
        final int PENALTY_VALUES_RETRY_SECONDS = 20;
        final boolean WITH_HISTORY = true;
        final int SECONDS_TO_SAVE_EMISSIONOFFENCES= 60;

        final int TIMES_TO_TRY_FOR_GETTING_LICENSCEPLATEAMOUNT = 3;
        final int SECONDS_TO_WAIT_BEFORE_RETRYING_LICENSCEPLATE = 2;

        final int DEFAULT_EMISSIONFACTOR = 55;
        final int DEFAULT_SPEEDFACTOR = 5;
        final int DEFAULT_HISTORYFACTOR =10;


        InputService inputservice = new RabbitMQInput("localhost", "GeneratedOffences");
        PenaltyService penaltyService = new PenaltyService(DEFAULT_EMISSIONFACTOR,DEFAULT_SPEEDFACTOR,DEFAULT_HISTORYFACTOR
                ,TIMES_TO_TRY_FOR_GETTING_LICENSCEPLATEAMOUNT,SECONDS_TO_WAIT_BEFORE_RETRYING_LICENSCEPLATE);
        Calculator calculator = new Calculator();
        SavedEmissionOffences savedEmissionOffences = new SavedEmissionOffences(SECONDS_TO_SAVE_EMISSIONOFFENCES);
        OutputService outputService = new RabbitMQOutput("localhost", "OffenceOutput");

        Controller controller = new Controller();
        controller.setInputService(inputservice);
        controller.setPenaltyService(penaltyService);
        controller.setCalculateWithHistory(WITH_HISTORY);
        controller.setCalculator(calculator);
        controller.setEmissionOffences(savedEmissionOffences);
        controller.setOutputService(outputService);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                penaltyService.getPenaltyValues();
            }
        }, 0, PENALTY_VALUES_RETRY_SECONDS *1000);

        controller.start();
    }
}
