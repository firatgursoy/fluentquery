package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.ValidationRegistry;

public interface FluentQueryFactory {
    FluentQuery newQuery();

    ValidationRegistry validationRegistry();
}
