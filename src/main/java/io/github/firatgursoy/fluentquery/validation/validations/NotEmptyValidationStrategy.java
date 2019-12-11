package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.ValidationUtil;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

import java.util.Collection;

public class NotEmptyValidationStrategy implements ValidationStrategy<Collection<?>, Boolean> {

    @Override
    public Boolean apply(Collection<?> objects) {
        return !ValidationUtil.isEmpty(objects);
    }
}