package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.separator.separators.*;

import java.util.function.Function;

@FunctionalInterface
public interface SeparatorStrategy extends Function<String, String> {

    public static Class<? extends SeparatorStrategy> LINUX = LinuxSeparatorStrategy.class;
    public static Class<? extends SeparatorStrategy> NONE = NoneSeparatorStrategy.class;
    public static Class<? extends SeparatorStrategy> SPACE = SpaceSeparatorStrategy.class;
    public static Class<? extends SeparatorStrategy> SYSTEM = SystemSeparatorStrategy.class;
    public static Class<? extends SeparatorStrategy> WINDOWS = WindowsSeparatorStrategy.class;

}
