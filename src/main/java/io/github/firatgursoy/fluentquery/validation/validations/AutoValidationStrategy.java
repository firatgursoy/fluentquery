package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

public class AutoValidationStrategy implements ValidationStrategy<Object, Boolean> {

    @Override
    public Boolean apply(Object o) {
        return false;
    }

    @Override
    public boolean isAuto() {
        return true;
    }
}