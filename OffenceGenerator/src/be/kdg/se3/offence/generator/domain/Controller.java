package be.kdg.se3.offence.generator.domain;

import be.kdg.se3.offence.generator.domain.entity.Offence;
import be.kdg.se3.offence.generator.domain.service.GeneratorService;
import be.kdg.se3.offence.generator.domain.service.OutputService;
import org.apache.log4j.Logger;

import java.util.Random;

public class Controller {
    private OutputService outputService;
    private GeneratorService generatorService;

    private boolean run = true;
    private Logger logger = Logger.getLogger(Controller.class);
    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }



    public void setGeneratorService(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public void stop() {
        this.run = false;
        try {
            outputService.shutdown();
        } catch (CommunicationException e) {
            logger.error("Unable to properly shut down communication channel");
        }
    }

    public void initialize() {
        try {
            outputService.initialize();
        } catch (CommunicationException e) {
           logger.error("Error during initialization of the output service");
        }

    }



    public void start(int lowerboundMilli,int upperboundMilli) {

        while (run) {
            int timeToWait=generateTimeToWait(lowerboundMilli,upperboundMilli);
            Offence offence = generatorService.generate();
            logger.info("Generated an offence of type: " + offence.getClass().toString());
            logger.debug("Offence content:" + offence.toString());

            try {
                outputService.publish(offence);
                logger.info("Waiting " + timeToWait/1000 + " seconds beforing generating again");
                Thread.sleep(timeToWait); // Wait the calculated amount of time before generating again
            } catch (Exception e) {
               logger.error("Error during generation of offences");
            }


        }
    }
    /**
     *  Returns a random time to wait before generating again with a value between the 2 parameters
     */
    private int generateTimeToWait(int lowerboundMilli,int upperboundMilli) {
        int timeToWait;
        if (lowerboundMilli == upperboundMilli) {
            timeToWait = lowerboundMilli;
        } else {
            Random random = new Random();
            timeToWait = random.nextInt(upperboundMilli-lowerboundMilli) + lowerboundMilli;
        }

        return  timeToWait;
    }
}
