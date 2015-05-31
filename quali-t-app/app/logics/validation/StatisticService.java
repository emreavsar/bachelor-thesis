package logics.validation;

import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import models.template.QAType;
import models.template.QAVar;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by emre on 30/05/15.
 */
public class StatisticService implements ValidationService {
    private static final Double MAX_DEVIATION = 0.1;

    @Override
    public HashMap<Long, List<String>> validateAll(List<Instance> qualityAttributes) {
        HashMap<Long, List<String>> validationWarnings = new HashMap<>();

        for (Instance qa : qualityAttributes) {

            // order values of the instance by varIndex
            ArrayList<Val> orderedValues = new ArrayList(qa.getValues());
            Collections.sort(orderedValues, (v1, v2) -> NumberUtils.compare(v1.getVarIndex(), v2.getVarIndex()));

            // order values of the instance by varIndex
            ArrayList<QAVar> orderedVariables = new ArrayList(qa.getTemplate().getVariables());
            Collections.sort(orderedVariables, (v1, v2) -> NumberUtils.compare(v1.getVarIndex(), v2.getVarIndex()));

            // iterate through vals and compare them with CatalogQA's QAVar
            for (int i = 0; i < orderedValues.size(); i++) {
                Val val = orderedValues.get(i);
                QAVar var = orderedVariables.get(i);
                ArrayList<String> warnings = new ArrayList<>();

                // do not check freetext
                if (!var.getType().equals(QAType.FREETEXT)) {
                    // average for numbers
                    if (var.getType().equals(QAType.ENUMNUMBER) || var.getType().equals(QAType.FREENUMBER)) {
                        Double avg = Double.valueOf(var.getAverageValue());
                        Double value = Double.valueOf(val.getValue());

                        // warn if value is more then average +- max_deviation
                        if (value > avg * (1 + MAX_DEVIATION) || value < avg * (1 - MAX_DEVIATION)) {
                            warnings.add("The average value (over all projects) for this quality attribute is "
                                    + avg + ", but you specified " + value + " which has an deviation of " + MAX_DEVIATION * 100 + "%");
                        }
                    }
                    // mostused for all
                    if (!var.getMosteUsedValue().equals(val.getValue())) {
                        warnings.add("The most used value (over all projects) for this quality attribute is "
                                + var.getMosteUsedValue() + ", but you specified " + val.getValue() + ".");
                    }
                    if (warnings.size() > 0) {
                        validationWarnings.put(qa.getId(), warnings);
                    }
                }
            }
        }

        return validationWarnings;
    }
}
