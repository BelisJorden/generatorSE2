package be.kdg.se3.offence.calculator.domain.entity;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Entity which contains past EmissionOffences, saved for an adjustable amount of time
 */
public class SavedEmissionOffences {

    private Logger logger = Logger.getLogger(SavedEmissionOffences.class);
    private Map<EmissionOffence,Timestamp> savedEmissionOffencesMap;
    private int timeToSave;

    public SavedEmissionOffences(int timeToSave) {
        this.savedEmissionOffencesMap = new HashMap<>();
        this.timeToSave = timeToSave;
    }

    public Map<EmissionOffence, Timestamp> getSavedEmissionOffencesMap() {
        return savedEmissionOffencesMap;
    }

    public int getTimeToSave() {
        return timeToSave;
    }

    public void addEmissionOffence(EmissionOffence emissionOffence, Timestamp timestamp) {
        this.savedEmissionOffencesMap.put(emissionOffence, timestamp);
    }

    public void removeOffence(EmissionOffence offence) {
        savedEmissionOffencesMap.remove(offence);
    }

    /**
     *  Removes the EmissionOffences from the map if the time to save them is expired
     */
    public void clearSavedEmissionOffencesIfExpired() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<EmissionOffence> emissionOffencesToRemove = new ArrayList<>();
        for (Map.Entry<EmissionOffence,Timestamp> pair : savedEmissionOffencesMap.entrySet()) {
            Timestamp savedTimestamp =  pair.getValue();
            long milliseconds = currentTimestamp.getTime() - savedTimestamp.getTime();
            int seconds = (int) milliseconds / 1000;
            if (seconds >= timeToSave) {
                emissionOffencesToRemove.add(pair.getKey());
                logger.info("EmissionOffence will be cleared after being saved for  " + timeToSave + " seconds");

            }
        }
        emissionOffencesToRemove.forEach(o -> savedEmissionOffencesMap.remove(o));




    }

    /**
     *  Checks if the incoming EmissionOffence happened in the same zone and with the same licenseplate a one of the saved EmissionOffences
     */
    public boolean checkSavedEmissionOffences(EmissionOffence emissionOffence) {
        boolean inSavedList =false;
        for (EmissionOffence savedEmissionOffence : savedEmissionOffencesMap.keySet()){
            if (emissionOffence.getLicencePlate().equals(savedEmissionOffence.getLicencePlate()) &&
                    emissionOffence.getCity().equals(savedEmissionOffence.getCity())) {
                inSavedList = true;

            }
        }
        return  inSavedList;
    }

}
