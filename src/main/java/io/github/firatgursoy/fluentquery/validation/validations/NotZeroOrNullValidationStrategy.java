package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.ValidationUtil;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

public class NotZeroOrNullValidationStrategy implements ValidationStrategy<Number, Boolean> {

    @Override
    public Boolean apply(Number number) {
        return !ValidationUtil.isNullOrZero(number);
    }
}
