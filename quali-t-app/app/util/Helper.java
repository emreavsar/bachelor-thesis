package util;

import com.google.common.base.Function;
import exceptions.EntityNotFoundException;
import models.project.nfritem.Instance;
import models.project.nfritem.Val;
import org.jsoup.Jsoup;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by corina on 23.04.2015.
 */
public class Helper {
    public Function<String, Long> parseLongFunction() {
        return ParseLongFunction.INSTANCE;
    }

    public boolean validate(String name) {
        return name != null && !name.isEmpty();
    }

    public String getDescriptionWithVars(Instance qualityAttributeInstance) throws EntityNotFoundException {
        Set<Val> values = qualityAttributeInstance.getValues();
        return replacePlaceholder(qualityAttributeInstance.getDescription(), values);
    }

    private String replacePlaceholder(String text, Set<Val> values) throws EntityNotFoundException {
        Pattern p = Pattern.compile("%VARIABLE_.*?([0-9]*)%");
        Matcher m = p.matcher(text);

        if (m.find()) {
            Val value = getVarValue(Integer.parseInt(m.group(1)), values);
            text = m.replaceFirst(value.getValue());
            return replacePlaceholder(text, values);
        }
        return text;
    }

    private Val getVarValue(int i, Set<Val> values) throws EntityNotFoundException {
        for (Val value : values) {
            if (value.getVarIndex() == i) {
                return value;
            }
        }
        throw new EntityNotFoundException("Value with this varindex not found!");
    }

    public String removeHtmlTags(String text) {

        return Jsoup.parse(text).text();
    }

    private enum ParseLongFunction implements Function<String, Long> {
        INSTANCE;

        public Long apply(String input) {
            return Long.valueOf(input);
        }
    }

}
