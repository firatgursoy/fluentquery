package io.github.firatgursoy.fluentquery;

import java.util.Map;

public interface ParameterMap {
    ParameterMap addValue(String key, Object value);

    ParameterMap addValues(Map<String, Object> values);
    
    Map<String, Object> getValues();

    Object getValue(String key);

    boolean hasValue(String key);
}
