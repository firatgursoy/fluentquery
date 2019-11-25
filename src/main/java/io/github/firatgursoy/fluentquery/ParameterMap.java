package io.github.firatgursoy.fluentquery;

import java.util.List;
import java.util.Map;

public interface ParameterMap {
    ParameterMap addValue(ValidationStrategy validationStrategy, String key, Object value);

    ParameterMap addValue(String key, Object value);

    List getValues();

    List<String> getKeys();

    ValidationStrategy getValidationStrategy(String key);

    boolean hasValue(String key);

    Map<String, Object> toMap();

    Object getValue(String paramKey);
}
