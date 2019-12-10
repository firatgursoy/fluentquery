package io.github.firatgursoy.fluentquery.validation;

import io.github.firatgursoy.fluentquery.validation.validations.*;

public enum Validations {
    AUTO {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return AutoValidationStrategy.class;
        }
    }, NONE {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NoneValidationStrategy.class;
        }
    }, NOT_BLANK {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotBlankValidationStrategy.class;
        }
    }, NOT_EMPTY {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotEmptyValidationStrategy.class;
        }
    }, NOT_NULL {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotNullValidationStrategy.class;
        }
    }, NOT_ZERO_OR_NULL {
        @Override
        Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotZeroOrNullValidationStrategy.class;
        }
    };

    abstract Class<? extends ValidationStrategy<?, Boolean>> getValidationClass();
}
