package io.github.firatgursoy.fluentquery;

public interface FluentQueryFactory {
    FluentQuery newQuery();

    FluentQuerySettingsHolder settingsHolder();
}
