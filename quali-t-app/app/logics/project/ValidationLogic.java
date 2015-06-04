package logics.project;

import com.google.inject.Inject;
import exceptions.EntityNotFoundException;
import exceptions.MissingParameterException;
import logics.validation.DetectorService;
import logics.validation.StatisticService;
import models.project.nfritem.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by emre on 04/06/15.
 */
public class ValidationLogic {
    @Inject
    DetectorService detectorService;
    @Inject
    StatisticService statisticService;
    @Inject
    ProjectLogic projectLogic;

    public HashMap<Long, HashMap<String, ArrayList<String>>> validate(Long projectId) throws MissingParameterException, EntityNotFoundException {
        List<Instance> instances = projectLogic.getQualityAttributes(projectId);
        HashMap<Long, HashMap<String, ArrayList<String>>> warnings = new HashMap<>();

        // get warnings
        HashMap<Long, List<String>> detectorWarnings = detectorService.validateAll(instances);
        HashMap<Long, List<String>> statisticWarnings = statisticService.validateAll(instances);

        for (Long instanceIdToWarn : detectorWarnings.keySet()) {
            // get the detector warnings for instance
            HashMap<String, ArrayList<String>> warning = new HashMap<>();
            // put all of them into a new hashmap with key detector
            warning.put("detector", (ArrayList<String>) detectorWarnings.get(instanceIdToWarn));
            // put instances warnings as new detector warnings
            warnings.put(instanceIdToWarn, warning);
        }

        // do the same for statistic warnings
        for (Long instanceIdToWarn : statisticWarnings.keySet()) {
            // if there are already warnings for the instance, add a new hashmap entry with different key (statistic)
            if (warnings.get(instanceIdToWarn) == null) {
                ArrayList<String> statisticWarning = (ArrayList<String>) statisticWarnings.get(instanceIdToWarn);
                // if there is a statistic warning for instance
                if (statisticWarning != null) {
                    HashMap<String, ArrayList<String>> warning = new HashMap<String, ArrayList<String>>();
                    warning.put("statistic", statisticWarning);
                    warnings.put(instanceIdToWarn, warning);
                }
            } else {
                HashMap<String, ArrayList<String>> warning = warnings.get(instanceIdToWarn);
                warning.put("statistic", (ArrayList<String>) statisticWarnings.get(instanceIdToWarn));
                warnings.put(instanceIdToWarn, warning);
            }
        }
        return warnings;
    }
}
