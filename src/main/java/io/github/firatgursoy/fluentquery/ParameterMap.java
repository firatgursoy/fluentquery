package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

import java.util.List;
import java.util.Map;

public interface ParameterMap {
    ParameterMap addValue(Class<? extends ValidationStrategy<?, Boolean>> validationStrategy, String key, Object value);

    ParameterMap addValue(String key, Object value);

    List getValues();

    List<String> getKeys();

    Class<? extends ValidationStrategy<?, Boolean>> getValidationStrategy(String key);

    boolean hasValue(String key);

    Map<String, Object> toMap();

    Object getValue(String paramKey);
}
