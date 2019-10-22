package io.github.firatgursoy.fluentquery;

import java.util.HashMap;
import java.util.Map;

public class ParameterMapImpl implements ParameterMap {
    private Map<String, Object> params = new HashMap<>();

    @Override public ParameterMap addValue(String key, Object value) {
        params.put(key, value);
        return this;
    }

    @Override public ParameterMap addValues(Map<String, Object> values) {
        params.putAll(values);
        return this;
    }

    @Override public Map<String, Object> getValues() {
        return params;
    }

    @Override public Object getValue(String key) {
        return params.get(key);
    }

    @Override public boolean hasValue(String key) {
        return params.containsKey(key);
    }
}
