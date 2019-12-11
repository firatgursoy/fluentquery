package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

public class NoneValidationStrategy implements ValidationStrategy<Object, Boolean> {

    @Override
    public Boolean apply(Object o) {
        return true;
    }
}