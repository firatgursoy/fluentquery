package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

import java.util.Objects;

public class NotNullValidationStrategy implements ValidationStrategy<Object, Boolean> {

    @Override
    public Boolean apply(Object o) {
        return Objects.nonNull(o);
    }

}