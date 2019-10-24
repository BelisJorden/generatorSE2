package be.kdg.se3.offence.generator.domain.service;

import be.kdg.se3.offence.generator.domain.CommunicationException;
import be.kdg.se3.offence.generator.domain.entity.Offence;

import java.io.IOException;

/**
 * A service that can be used to publish messages to a communication interface (e.g. message queue)
 */
public interface OutputService {

    /**
     * Initializes the communication interface at the start
     */
    void initialize() throws CommunicationException;

    /**
     * Publishes messages to the communication interface
     */
    void publish(Offence offence) throws IOException,CommunicationException;

    /**
     * Close all connections to this service
     */
    void shutdown() throws CommunicationException;
}
