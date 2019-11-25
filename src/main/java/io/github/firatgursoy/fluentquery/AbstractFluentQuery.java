package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.validation.ValidationRegistry;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractFluentQuery implements FluentQuery {

    protected ParameterMap params = new ParameterMapImpl();
    protected List<Object> beanPropertyParameterObjs = new ArrayList<>();
    protected ValidationStrategy defaultValidationStrategy = ValidationStrategy.none();
    private List<PreparingQuery> preparingQueries = new LinkedList<>();
    private SeparatorStrategy separatorStrategy = SeparatorStrategy.eolLinux();

    private ValidationRegistry validationRegistry;

    public AbstractFluentQuery(ValidationRegistry validationRegistry) {
        this.validationRegistry = validationRegistry;
    }

    protected Map.Entry<String, Map<String, Object>> prepare() {
        fetchBeanPropertyParameterSource();
        StringBuilder sql = new StringBuilder();
        StringBuilder messages = new StringBuilder();
        for (PreparingQuery preparingQuery : preparingQueries) {
            for (PreparingQuery.ParamConditionPair paramConditionPair : preparingQuery.paramConditionPairs) {
                Object value = params.getValue(paramConditionPair.paramKey);
                ValidationStrategy<Object, Boolean> validationStrategy = paramConditionPair.validationStrategy;
                if (validationStrategy.isAuto()) {
                    validationStrategy = validationRegistry.getValidationStrategy(value.getClass());
                }
                if (!validationStrategy.apply(value)) {
                    messages.append(paramConditionPair.paramKey + "'s value must be valid. [Value = " + value + "], [ValidationStrategy = " + validationStrategy + "]\n");
                }
            }
            sql.append(separatorStrategy.apply(preparingQuery.sqlPart));
        }
        if (!messages.toString().isEmpty()) {
            throw new RuntimeException(messages.toString());
        }
        return new HashMap.SimpleEntry<>(sql.toString(), params.toMap());
    }

    @Override
    public FluentQuery defaultValidationStrategy(ValidationStrategy validationStrategy) {
        this.defaultValidationStrategy = validationStrategy;
        return this;
    }

    @Override
    public FluentQuery separatorStrategy(SeparatorStrategy separatorStrategy) {
        this.separatorStrategy = separatorStrategy;
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params, ValidationStrategy validationStrategy) {
        List<String> strings = ValidationUtil.extractParameterKeys(sqlPart);
        List<PreparingQuery.ParamConditionPair> pairs = new LinkedList<>();
        for (String key : strings) {
            pairs.add(new PreparingQuery.ParamConditionPair(key, validationStrategy));
        }
        preparingQueries.add(new PreparingQuery(sqlPart, pairs));
        if (params != null) {
            this.params = params.apply(this.params);
        }
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart) {
        return append(sqlPart, (params) -> params, defaultValidationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, ValidationStrategy validationStrategy) {
        return append(sqlPart, (params) -> params, validationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue) {
        return append(sqlPart, paramKey, paramValue, defaultValidationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue, ValidationStrategy validationStrategy) {
        return append(sqlPart, (params) -> params.addValue(ValidationStrategy.none(), paramKey, paramValue), validationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, Function<ParameterMap, ParameterMap> params) {
        return append(sqlPart, params, defaultValidationStrategy);
    }

    @Override
    public FluentQuery param(String key, Object value) {
        return param(key, value, defaultValidationStrategy);
    }

    @Override
    public FluentQuery param(Object beanPropertyParameterObj) {
        this.beanPropertyParameterObjs.add(beanPropertyParameterObj);
        return this;
    }

    @Override
    public FluentQuery param(String key, Object value, ValidationStrategy validationStrategy) {
        this.params.addValue(validationStrategy, key, value);
        return this;
    }

    @Override
    public FluentQuery compose(FluentQuery query) {
        ParameterMap params = ((AbstractFluentQuery) query).params;
        for (String key : params.getKeys()) {
            this.params.addValue(params.getValidationStrategy(key), key, params.getValue(key));
        }
        List<Object> beanPropertyParameterObjs = ((AbstractFluentQuery) query).beanPropertyParameterObjs;
        this.beanPropertyParameterObjs.addAll(beanPropertyParameterObjs);
        this.preparingQueries.addAll(((AbstractFluentQuery) query).preparingQueries);
        return this;
    }

    public abstract int update();

    public abstract <T> List<T> list(Class<T> mappedClass);

    public abstract <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addChildSupplier);

    public abstract <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addSubClassBiConsumer, String idColumnLabel);

    public abstract <T> Optional<T> getOptional(Class<T> mappedClass);

    public abstract <T> T get(Class<T> mappedClass);

    public abstract void fetchBeanPropertyParameterSource();

    private static class PreparingQuery {
        private String sqlPart;
        private List<ParamConditionPair> paramConditionPairs = new ArrayList<>();

        PreparingQuery(String sqlPart, List<ParamConditionPair> paramConditionPairs) {
            this.sqlPart = sqlPart;
            this.paramConditionPairs = paramConditionPairs;
        }

        PreparingQuery(String sqlPart) {
            this.sqlPart = sqlPart;
        }

        public String getSqlPart() {
            return sqlPart;
        }

        public void setSqlPart(String sqlPart) {
            this.sqlPart = sqlPart;
        }

        public List<ParamConditionPair> getParamConditionPairs() {
            return paramConditionPairs;
        }

        public void setParamConditionPairs(List<ParamConditionPair> paramConditionPairs) {
            this.paramConditionPairs = paramConditionPairs;
        }

        protected static class ParamConditionPair {
            private String paramKey;
            private ValidationStrategy<Object, Boolean> validationStrategy = ValidationStrategy.none();

            protected ParamConditionPair() {

            }

            ParamConditionPair(String paramKey, ValidationStrategy<Object, Boolean> validationStrategy) {
                this.paramKey = paramKey;
                this.validationStrategy = validationStrategy;
            }

            public ValidationStrategy<Object, Boolean> getValidationStrategy() {
                return validationStrategy;
            }

            public String getParamKey() {
                return paramKey;
            }
        }
    }
}
