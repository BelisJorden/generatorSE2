package be.kdg.se3.offence.generator.domain;

/**
 * Used for anything that goes wrong during communication with the external communication interface
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }
}
