package be.kdg.se3.offence.calculator.domain.service;



import be.kdg.se3.offence.calculator.domain.entity.Offence;

/**
 * Callback interface for incoming messages
 */
public interface InputListener {
    /**
     *
     * Called by the InputListener when a new message arrives
     * @param offence
     */
    void onReceive(Offence offence);
}
