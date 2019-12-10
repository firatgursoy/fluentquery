package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import io.github.firatgursoy.fluentquery.validation.validations.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public interface FluentQuery {

    FluentQuery append(String sqlPart);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy);

    FluentQuery append(String sqlPart, ParameterMapConsumer<ParameterMap> params);

    FluentQuery param(String key, Object value);

    FluentQuery param(String key, Object value, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy);

    FluentQuery param(Object beanPropertySource);

    FluentQuery compose(FluentQuery query);

    FluentQuerySettingsHolder settingsHolder();

    <T> List<T> list(Class<T> mappedClass);

    <T> Optional<T> getOptional(Class<T> mappedClass);

    <T> T get(Class<T> mappedClass);

    int update();

    <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addChildSupplier);

    <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addSubClassBiConsumer, String idColumnLabel);

}
