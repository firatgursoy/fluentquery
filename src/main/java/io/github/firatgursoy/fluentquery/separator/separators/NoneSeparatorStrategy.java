package io.github.firatgursoy.fluentquery.separator.separators;

import io.github.firatgursoy.fluentquery.SeparatorStrategy;

public class NoneSeparatorStrategy implements SeparatorStrategy {

    @Override
    public String apply(String s) {
        return s;
    }
}
