package logics.validation;

import ch.qualit.fuzziness.detector.spi.FuzzinessDetector;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import play.Logger;

import java.util.*;

public class DetectorService implements ValidationService {
    private ServiceLoader<FuzzinessDetector> loader;

    public DetectorService() {
        loader = ServiceLoader.load(FuzzinessDetector.class);
    }

    public HashMap<Long, List<String>> validateAll(List<Instance> qualityAttributes) {
        Logger.info("validateAll called");
        HashMap<Long, List<String>> detections = new HashMap<>();

        try {
            for (Instance instance : qualityAttributes) {
                // get detectors and iterate through them
                Iterator<FuzzinessDetector> detectors = loader.iterator();
                Logger.debug("detectors found = " + detectors.hasNext());
                while (detectors.hasNext()) {
                    FuzzinessDetector detector = detectors.next();
                    Logger.debug("Detector: " + detector.getClass().getName());

                    ArrayList<String> suggestions = (ArrayList<String>) detector
                            .validate(instance.getDescription());

                    // validate variables
                    for (Val val : instance.getValues()) {
                        suggestions.addAll(detector.validate(val.getValue()));
                    }

                    if (!suggestions.isEmpty()) {
                        Logger.debug("Suggestions for the word found: " + instance.getDescription()
                                + " are: " + suggestions.toString());

                        detections.put(instance.getId(), suggestions);

                    } else {
                        Logger.debug("No suggestion found!");
                    }
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            serviceError.printStackTrace();
        }
        return detections;
    }
}
