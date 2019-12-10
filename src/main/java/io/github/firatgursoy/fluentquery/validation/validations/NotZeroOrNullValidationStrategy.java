package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.ValidationUtil;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

public class NotZeroOrNullValidationStrategy implements ValidationStrategy<Comparable<Number>, Boolean> {

    @Override
    public Boolean apply(Comparable<Number> numberComparable) {
        return !ValidationUtil.isNullOrZero(numberComparable);
    }
}
