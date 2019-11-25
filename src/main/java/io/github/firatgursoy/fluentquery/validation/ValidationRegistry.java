package io.github.firatgursoy.fluentquery.validation;

import io.github.firatgursoy.fluentquery.ValidationStrategy;

public interface ValidationRegistry {
    <T> void addValidationStrategy(Class<T> clazz, ValidationStrategy validationStrategy);

    <T> ValidationStrategy getValidationStrategy(Class<T> type);
}
