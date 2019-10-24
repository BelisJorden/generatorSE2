package be.kdg.se3.offence.calculator.domain;


public class CommunicationException extends RuntimeException {

    public CommunicationException(String message, Exception cause) {
        super(message, cause);
    }
}
