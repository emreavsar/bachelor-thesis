package api;

import ch.qualit.fuzziness.detector.spi.FuzzynessDetector;
import models.project.nfritem.Instance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public class DetectorService {
    private ServiceLoader<FuzzynessDetector> loader;

    public DetectorService() {
        loader = ServiceLoader.load(FuzzynessDetector.class);
    }

    public String validateAll(ArrayList<Instance> qualityAttributes) {
        String toReturn = "";
        try {
            for (Instance instance : qualityAttributes) {
                String desc = instance.getDescription();
                toReturn += "Going to validate this: " + desc;

                Iterator<FuzzynessDetector> dictionaries = loader.iterator();
                while (dictionaries.hasNext()) {
                    FuzzynessDetector detector = dictionaries.next();
                    toReturn += "Detector: " + detector.getClass().getName();
                    ArrayList<String> suggestions = (ArrayList<String>) detector
                            .validate(desc);
                    if (!suggestions.isEmpty()) {
                        toReturn += "Suggestions for the word: " + desc
                                + " are: " + suggestions.toString();
                    } else {
                        toReturn += "No suggestion found!";
                    }
                    toReturn += "\n";
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
        }
        return toReturn;
    }
}
