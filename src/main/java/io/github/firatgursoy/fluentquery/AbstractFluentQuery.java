package io.github.firatgursoy.fluentquery;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractFluentQuery implements FluentQuery {

    private StringBuilder sql = new StringBuilder();
    private ParameterMap params = new ParameterMapImpl();

    public StringBuilder getSql() {
        return sql;
    }

    public ParameterMap getParams() {
        return params;
    }

    Map<ValidationStrategy, Function<Object, Boolean>> validationStrategies = new HashMap<ValidationStrategy, Function<Object, Boolean>>() {{
        put(ValidationStrategy.NOT_NULL, Objects::nonNull);
        put(ValidationStrategy.NOT_BLANK, (obj) -> !ValidationUtil.isBlank(obj.toString()));
        put(ValidationStrategy.NOT_EMPTY, (obj) -> !ValidationUtil.isEmpty((Collection<?>) obj));
        put(ValidationStrategy.NOT_ZERO_OR_NULL, (obj) -> obj != null && ((BigDecimal) obj).compareTo(BigDecimal.ZERO) != 0);

        put(ValidationStrategy.AUTO, (obj) -> {
            if (obj instanceof Collection) {
                return this.get(ValidationStrategy.NOT_EMPTY).apply(obj);
            } else if (obj instanceof Date) {
                return this.get(ValidationStrategy.NOT_NULL).apply(obj);
            } else if (obj instanceof String) {
                return this.get(ValidationStrategy.NOT_BLANK).apply(obj);
            } else if (obj instanceof BigDecimal) {
                return this.get(ValidationStrategy.NOT_ZERO_OR_NULL).apply(obj);
            } else {
                return false;
            }
        });
        put(ValidationStrategy.DISABLE, (obj) -> true);
    }};

    ValidationStrategy defaultValidationStrategy = ValidationStrategy.DISABLE;

    @Override
    public FluentQuery append(String sqlPart) {
        sql.append(sqlPart).append("\n");
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart, Supplier condition) {
        if ((Boolean) condition.get()) {
            sql.append(sqlPart).append("\n");
        }
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue) {
        return append(sqlPart, paramKey, paramValue, defaultValidationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue, FluentQuery.ValidationStrategy validationStrategy) {
        if (validationStrategies.get(validationStrategy).apply(paramValue)) {
            sql.append(sqlPart).append("\n");
            this.params.addValue(paramKey, paramValue);
        }
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart, Supplier condition, Function<ParameterMap, ParameterMap> params) {
        if ((Boolean) condition.get()) {
            sql.append(sqlPart).append("\n");
            params.apply(this.params);
        }
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params) {
        sql.append(sqlPart).append("\n");
        params.apply(this.params);
        return this;
    }

    @Override
    public FluentQuery param(String key, Object value) {
        this.params.addValue(key, value);
        return this;
    }

    @Override
    public FluentQuery param(String key, Object value, FluentQuery.ValidationStrategy validationStrategy) {
        if (validationStrategies.get(validationStrategy).apply(value)) {
            this.params.addValue(key, value);
            return this;
        } else {
            throw new RuntimeException(key + "'s value must be valid. Current value : " + value);
        }
    }

    @Override
    public FluentQuery compose(FluentQuery query) {
        this.params.addValues(((AbstractFluentQuery) query).params.getValues());
        this.sql.append(((AbstractFluentQuery) query).sql);
        return this;
    }

    @Override
    public FluentQuery defaultValidationStrategy(FluentQuery.ValidationStrategy defaultValidationStrategy) {
        this.defaultValidationStrategy = defaultValidationStrategy;
        return this;
    }

    public abstract <T> List<T> list(Class<T> mappedClass);

    public abstract <T> Optional<T> getOptional(Class<T> mappedClass);

    public abstract <T> T get(Class<T> mappedClass);
}
