package io.github.firatgursoy.fluentquery.validation;

public interface ValidationRegistry {
    <T> void addValidationStrategy(Class<T> clazz, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy);

    <T> Class<? extends ValidationStrategy<?, Boolean>> getValidationStrategy(Class<T> type);
}
