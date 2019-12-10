package io.github.firatgursoy.fluentquery;

import java.util.function.Consumer;

@FunctionalInterface
public interface ParameterMapConsumer<T extends ParameterMap> extends Consumer<ParameterMap> {
    @Override
    void accept(ParameterMap parameterMap);
}
