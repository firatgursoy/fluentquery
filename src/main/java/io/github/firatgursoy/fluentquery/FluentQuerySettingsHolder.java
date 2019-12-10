package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.DefaultValidationRegistry;
import io.github.firatgursoy.fluentquery.validation.ValidationRegistry;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;


public class FluentQuerySettingsHolder {
    private ValidationRegistry validationRegistry = new DefaultValidationRegistry();
    private Class<? extends ValidationStrategy<?, Boolean>> defaultValidationStrategy = ValidationStrategy.NONE;
    private Class<? extends SeparatorStrategy> defaultSeparatorStrategy = SeparatorStrategy.LINUX;

    public ValidationRegistry getValidationRegistry() {
        return validationRegistry;
    }

    public FluentQuerySettingsHolder setValidationRegistry(ValidationRegistry validationRegistry) {
        this.validationRegistry = validationRegistry;
        return this;
    }

    public Class<? extends ValidationStrategy<?, Boolean>> getDefaultValidationStrategy() {
        return defaultValidationStrategy;
    }

    public FluentQuerySettingsHolder setDefaultValidationStrategy(Class<? extends ValidationStrategy<?, Boolean>> defaultValidationStrategy) {
        this.defaultValidationStrategy = defaultValidationStrategy;
        return this;
    }

    public Class<? extends SeparatorStrategy> getDefaultSeparatorStrategy() {
        return defaultSeparatorStrategy;
    }

    public FluentQuerySettingsHolder setDefaultSeparatorStrategy(Class<? extends SeparatorStrategy> defaultSeparatorStrategy) {
        this.defaultSeparatorStrategy = defaultSeparatorStrategy;
        return this;
    }
}
