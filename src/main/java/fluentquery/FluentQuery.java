package fluentquery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public interface FluentQuery {


    enum ValidationStrategy {
        NOT_NULL, NOT_BLANK, NOT_EMPTY, NOT_ZERO_OR_NULL, AUTO, DISABLE
    }

    FluentQuery append(String sqlPart);

    FluentQuery append(String sqlPart, Supplier condition);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue);

    FluentQuery append(String sqlPart, String paramKey, Object paramValue, ValidationStrategy validationStrategy);

    FluentQuery append(String sqlPart, Supplier condition, Function<ParameterMap, ParameterMap> params);

    FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params);

    FluentQuery param(String key, Object value);

    FluentQuery param(String key, Object value, ValidationStrategy validationStrategy);

    FluentQuery compose(FluentQuery query);

    FluentQuery defaultValidationStrategy(ValidationStrategy defaultValidationStrategy);

    String toSql();
    
    <T> List<T> list(Class<T> mappedClass);

    <T> Optional<T> getOptional(Class<T> mappedClass);

    <T> T get(Class<T> mappedClass);
}
