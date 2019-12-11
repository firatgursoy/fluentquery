package io.github.firatgursoy.fluentquery.validation;

import java.util.*;

public class DefaultValidationRegistry implements ValidationRegistry {

    private Map<Class, Class<? extends ValidationStrategy<?, Boolean>>> validationStrategies = new LinkedHashMap<>();

    public DefaultValidationRegistry() {
        addDefaultValidationStrategies();
    }

    void addDefaultValidationStrategies() {
        addValidationStrategy(CharSequence.class, ValidationStrategy.NOT_BLANK);
        addValidationStrategy(Number.class, ValidationStrategy.NOT_ZERO_OR_NULL);
        addValidationStrategy(Collection.class, ValidationStrategy.NOT_EMPTY);
        addValidationStrategy(Date.class, ValidationStrategy.NOT_NULL);
        addValidationStrategy(Boolean.class, ValidationStrategy.NOT_NULL);
    }


    @Override
    public <T> void addValidationStrategy(Class<T> clazz, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy) {
        validationStrategies.put(clazz, validationStrategy);
    }

    @Override
    public <T> Class<? extends ValidationStrategy<?, Boolean>> getValidationStrategy(Class<T> type) {

        Optional<Map.Entry<Class, Class<? extends ValidationStrategy<?, Boolean>>>> first = validationStrategies.entrySet().stream().filter(clazz -> clazz.getKey().isAssignableFrom(type)).findFirst();
        if (first.isPresent()) {
            return first.get().getValue();
        }
        return ValidationStrategy.NONE;
    }

}
