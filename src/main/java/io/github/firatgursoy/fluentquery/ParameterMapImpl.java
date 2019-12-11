package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterMapImpl implements ParameterMap {
    private Map<String, Object> params = new HashMap<>();
    private Map<String, Class<? extends ValidationStrategy<?, Boolean>>> validationStrategies = new HashMap<>();

    @Override
    public ParameterMap addValue(Class<? extends ValidationStrategy<?, Boolean>> validationStrategy, String key, Object value) {
        params.put(key, value);
        validationStrategies.put(key, validationStrategy);
        return this;
    }

    @Override
    public ParameterMap addValue(String key, Object value) {
        return addValue(ValidationStrategy.AUTO, key, value);
    }

    @Override
    public List getValues() {
        return new ArrayList<>(params.values());
    }

    @Override
    public List<String> getKeys() {
        return new ArrayList<>(params.keySet());
    }

    @Override
    public Object getValue(String key) {
        return params.get(key);
    }

    @Override
    public Class<? extends ValidationStrategy<?, Boolean>> getValidationStrategy(String key) {
        return validationStrategies.get(key);
    }

    @Override
    public boolean hasValue(String key) {
        return params.containsKey(key);
    }

    @Override
    public Map<String, Object> toMap() {
        return params;
    }
}
