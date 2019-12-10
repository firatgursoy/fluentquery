package io.github.firatgursoy.fluentquery;

public abstract class AbstractFluentQueryFactory implements FluentQueryFactory {

    private FluentQuerySettingsHolder settingsHolder;

    @Override
    public FluentQuerySettingsHolder settingsHolder() {
        if (settingsHolder == null) {
            settingsHolder = new FluentQuerySettingsHolder();
        }
        return settingsHolder;
    }
}
