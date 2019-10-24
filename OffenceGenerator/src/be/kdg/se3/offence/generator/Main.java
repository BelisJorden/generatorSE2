package be.kdg.se3.offence.generator;

import be.kdg.se3.offence.generator.adapter.RabbitMQ;
import be.kdg.se3.offence.generator.domain.Controller;
import be.kdg.se3.offence.generator.domain.Generator;
import be.kdg.se3.offence.generator.domain.service.GeneratorService;
import be.kdg.se3.offence.generator.domain.service.OutputService;

public class Main {
    public static void main(String[] args) {
        final int LOWER_BOUND_MILLI = 1000;
        final int UPPER_BOUND_MILLI = 3000;
        final int SPEED_OFFENCE_PERCENTAGE = 40;
        final int SPEED_UPPER_LIMIT = 200;
        final int[] MAX_SPEED_LIST = {30,50,70,120};
        final String[] CITY_LIST = {"Antwerpen", "Brussel", "Mechelen", "Aartselaar", "Leuven", "Lier", "Bornem"};
        final String[] LICENSE_LIST = {"1-UNK-123", "1-ERR-123", "1-AKD-845", "2-ASF-247", "1-DLJ-458", "1-MKN-291", "1-PDO-746"};

        Controller controller = new Controller();
        GeneratorService generator = new Generator(SPEED_OFFENCE_PERCENTAGE,MAX_SPEED_LIST,CITY_LIST,LICENSE_LIST,SPEED_UPPER_LIMIT);
        OutputService outputService = new RabbitMQ("localhost", "GeneratedOffences");
        controller.setGeneratorService(generator);
        controller.setOutputService(outputService);
        controller.initialize();
        controller.start(LOWER_BOUND_MILLI,UPPER_BOUND_MILLI);

    }
}
