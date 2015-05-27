package api;

import ch.qualit.fuzziness.detector.spi.FuzzynessDetector;
import models.project.nfritem.Instance;
import play.Logger;

import java.util.*;

public class DetectorService {
    private ServiceLoader<FuzzynessDetector> loader;

    public DetectorService() {
        loader = ServiceLoader.load(FuzzynessDetector.class);
    }

    public HashMap<Long, List<String>> validateAll(List<Instance> qualityAttributes) {
        Logger.info("validateAll called");
        HashMap<Long, List<String>> detections = new HashMap<>();

        try {
            for (Instance instance : qualityAttributes) {
                // get detectors and iterate through them
                Iterator<FuzzynessDetector> detectors = loader.iterator();
                Logger.debug("detectors found = " + detectors.hasNext());
                while (detectors.hasNext()) {
                    FuzzynessDetector detector = detectors.next();
                    Logger.debug("Detector: " + detector.getClass().getName());

                    ArrayList<String> suggestions = (ArrayList<String>) detector
                            .validate(instance.getDescription());

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
