package io.github.firatgursoy.fluentquery.validation;

import io.github.firatgursoy.fluentquery.ValidationStrategy;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultValidationRegistry implements ValidationRegistry {

    private Map<Class, ValidationStrategy> validationStrategies = new LinkedHashMap<>();

    public DefaultValidationRegistry() {
        addDefaultValidationStrategies();
    }

    void addDefaultValidationStrategies() {
        addValidationStrategy(CharSequence.class, ValidationStrategy.notBlank());
        addValidationStrategy(Date.class, ValidationStrategy.notNull());
        addValidationStrategy(Number.class, ValidationStrategy.notZeroOrNull());
        addValidationStrategy(Collection.class, ValidationStrategy.notEmpty());
        addValidationStrategy(Boolean.class, ValidationStrategy.notNull());
    }

    @Override
    public <T> void addValidationStrategy(Class<T> clazz, ValidationStrategy validationStrategy) {
        if (getValidationStrategy(clazz).equals(ValidationStrategy.none())) {
            validationStrategies.put(clazz, validationStrategy);
        }
    }

    @Override
    public <T> ValidationStrategy getValidationStrategy(Class<T> type) {
        ValidationStrategy validationStrategy = validationStrategies.get(type);
        if (validationStrategy == null) {
            return ValidationStrategy.none();
        }
        return validationStrategy;
    }

}
