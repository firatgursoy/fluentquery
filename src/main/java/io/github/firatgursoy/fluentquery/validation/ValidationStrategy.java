package io.github.firatgursoy.fluentquery.validation;

import io.github.firatgursoy.fluentquery.validation.validations.*;

import java.util.Collection;
import java.util.function.Function;

@FunctionalInterface
public interface ValidationStrategy<T, R extends Boolean> extends Function<T, R> {
    public static final Class<? extends ValidationStrategy<?, Boolean>> AUTO = AutoValidationStrategy.class;
    public static final Class<? extends ValidationStrategy<?, Boolean>> NONE = NoneValidationStrategy.class;
    public static final Class<? extends ValidationStrategy<?, Boolean>> NOT_BLANK = NotBlankValidationStrategy.class;
    public static final Class<? extends ValidationStrategy<?, Boolean>> NOT_EMPTY = NotEmptyValidationStrategy.class;
    public static final Class<? extends ValidationStrategy<?, Boolean>> NOT_NULL = NotNullValidationStrategy.class;
    public static final Class<? extends ValidationStrategy<?, Boolean>> NOT_ZERO_OR_NULL = NotZeroOrNullValidationStrategy.class;

    default boolean isAuto() {
        return false;
    }

}