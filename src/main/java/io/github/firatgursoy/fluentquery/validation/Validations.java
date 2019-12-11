package io.github.firatgursoy.fluentquery.validation;

import io.github.firatgursoy.fluentquery.validation.validations.*;

public enum Validations {
    AUTO {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return AutoValidationStrategy.class;
        }
    }, NONE {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NoneValidationStrategy.class;
        }
    }, NOT_BLANK {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotBlankValidationStrategy.class;
        }
    }, NOT_EMPTY {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotEmptyValidationStrategy.class;
        }
    }, NOT_NULL {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotNullValidationStrategy.class;
        }
    }, NOT_ZERO_OR_NULL {
        @Override
        public Class<? extends ValidationStrategy<?, Boolean>> getValidationClass() {
            return NotZeroOrNullValidationStrategy.class;
        }
    };

    public abstract Class<? extends ValidationStrategy<?, Boolean>> getValidationClass();
}
