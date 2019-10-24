package be.kdg.se3.offence.generator.domain.service;

import be.kdg.se3.offence.generator.domain.entity.Offence;

/**
 * A service that can be used to generate offences
 */
public interface GeneratorService {

    /**
     * Returns a generated Offence
     */
    Offence generate();
}
