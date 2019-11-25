package io.github.firatgursoy.fluentquery;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public interface FluentQuery {

    FluentQuery append(String sqlPart);

    FluentQuery append(String sqlPart, ValidationStrategy condition);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue, ValidationStrategy validationStrategy);

    FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params, ValidationStrategy validationStrategy);

    FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params);

    FluentQuery param(String key, Object value);

    FluentQuery param(String key, Object value, ValidationStrategy validationStrategy);

    FluentQuery param(Object beanPropertySource);

    FluentQuery compose(FluentQuery query);

    FluentQuery defaultValidationStrategy(ValidationStrategy defaultValidationStrategy);

    FluentQuery separatorStrategy(SeparatorStrategy separatorStrategy);

    <T> List<T> list(Class<T> mappedClass);

    <T> Optional<T> getOptional(Class<T> mappedClass);

    <T> T get(Class<T> mappedClass);

    int update();

    <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addChildSupplier);

    <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addSubClassBiConsumer, String idColumnLabel);

}
