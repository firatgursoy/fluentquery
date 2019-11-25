package io.github.firatgursoy.fluentquery;

import java.util.function.Function;

@FunctionalInterface
public interface SeparatorStrategy extends Function<String, String> {

    static SeparatorStrategy eolLinux() {
        return s -> s + "\n";
    }

    static SeparatorStrategy eolWindows() {
        return s -> s + "\r\n";
    }

    static SeparatorStrategy space() {
        return s -> s + " ";
    }

    static SeparatorStrategy system() {
        return s -> s + System.lineSeparator();
    }

    static SeparatorStrategy none() {
        return s -> s;
    }
}
