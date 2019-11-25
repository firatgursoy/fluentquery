package io.github.firatgursoy.fluentquery;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface ValidationStrategy<T, R extends Boolean> extends Function<T, R> {
    static ValidationStrategy<Object, Boolean> notNull() {
        return Objects::nonNull;
    }

    static ValidationStrategy<CharSequence, Boolean> notBlank() {
        return (str) -> !ValidationUtil.isBlank(str);
    }

    static ValidationStrategy<Collection<?>, Boolean> notEmpty() {
        return (coll) -> !ValidationUtil.isEmpty(coll);
    }

    static ValidationStrategy<Comparable<Number>, Boolean> notZeroOrNull() {
        return (number) -> !ValidationUtil.isNullOrZero(number);
    }

    static ValidationStrategy auto() {
        return new ValidationStrategy<Object, Boolean>() {
            @Override
            public Boolean apply(Object o) {
                return false;
            }

            @Override
            public boolean isAuto() {
                return true;
            }
        };
    }

    static ValidationStrategy none() {
        return (val) -> Boolean.TRUE;
    }

    default boolean isAuto() {
        return false;
    }

}