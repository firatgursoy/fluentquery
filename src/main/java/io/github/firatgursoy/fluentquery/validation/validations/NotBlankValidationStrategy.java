package io.github.firatgursoy.fluentquery.validation.validations;

import io.github.firatgursoy.fluentquery.ValidationUtil;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;

public class NotBlankValidationStrategy implements ValidationStrategy<CharSequence, Boolean> {

    @Override
    public Boolean apply(CharSequence str) {
        return !ValidationUtil.isBlank(str);
    }
}