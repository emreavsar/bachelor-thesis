package logics.validation;

import models.project.nfritem.Instance;

import java.util.HashMap;
import java.util.List;

/**
 * Created by emre on 30/05/15.
 */
public interface ValidationService {
    public HashMap<Long, List<String>> validateAll(List<Instance> qualityAttributes);
}
