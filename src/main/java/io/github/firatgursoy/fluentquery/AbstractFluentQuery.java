package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.annotation.Validate;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import io.github.firatgursoy.fluentquery.validation.validations.NotNullValidationStrategy;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractFluentQuery implements FluentQuery {

    protected ParameterMap params = new ParameterMapImpl();
    protected List<Object> beanPropertyParameterObjs = new ArrayList<>();

    private List<PreparingQuery> preparingQueries = new LinkedList<>();

    private FluentQuerySettingsHolder settingsHolder;

    public AbstractFluentQuery(FluentQuerySettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }

    protected Map.Entry<String, Map<String, Object>> prepare() {
        fetchBeanPropertyParameterSource();
        StringBuilder sql = new StringBuilder();
        StringBuilder messages = new StringBuilder();
        for (PreparingQuery preparingQuery : preparingQueries) {
            boolean notRequiredValidationProblem = false;
            for (PreparingQuery.ParamConditionPair paramConditionPair : preparingQuery.paramConditionPairs) {
                Object value = params.getValue(paramConditionPair.paramKey);
                Class<? extends ValidationStrategy<?, Boolean>> validationStrategyClazz = paramConditionPair.validationStrategy;
                if (params.getValidationStrategy(paramConditionPair.paramKey) != null) {
                    validationStrategyClazz = params.getValidationStrategy(paramConditionPair.paramKey);
                }
                if (validationStrategyClazz == null) {
                    validationStrategyClazz = settingsHolder().getDefaultValidationStrategy();
                }
                ValidationStrategy<Object, Boolean> validationStrategy = (ValidationStrategy<Object, Boolean>) ValidationUtil.instantiateClass(validationStrategyClazz);
                if (validationStrategy.isAuto()) {
                    if (value == null) {
                        validationStrategy = ValidationUtil.instantiateClass(NotNullValidationStrategy.class);
                    } else {
                        validationStrategy = (ValidationStrategy<Object, Boolean>) ValidationUtil.instantiateClass(settingsHolder.getValidationRegistry().getValidationStrategy(value.getClass()));
                    }
                }
                if (params.getValidationStrategy(paramConditionPair.paramKey) != null && !validationStrategy.apply(value)) {
                    for (Object beanPropertyParameterObj : beanPropertyParameterObjs) {
                        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(beanPropertyParameterObj);
                        TypeDescriptor propertyTypeDescriptor = bw.getPropertyTypeDescriptor(paramConditionPair.paramKey);
                        if (propertyTypeDescriptor.hasAnnotation(Validate.class)) {
                            Validate annotation = propertyTypeDescriptor.getAnnotation(Validate.class);
                            if (annotation.optional()) {
                                notRequiredValidationProblem = true;
                            } else {
                                messages.append(paramConditionPair.paramKey + "'s value must be valid. [Value = " + value + "], [ValidationStrategy = " + validationStrategy + "]\n");
                            }
                        }
                    }

                } else if (params.getValidationStrategy(paramConditionPair.paramKey) == null && !validationStrategy.apply(value)) {
                    notRequiredValidationProblem = true;
                }
            }
            if (!notRequiredValidationProblem) {
                sql.append(ValidationUtil.instantiateClass(settingsHolder.getDefaultSeparatorStrategy()).apply(preparingQuery.sqlPart));
            }
        }
        if (!messages.toString().isEmpty()) {
            throw new RuntimeException(messages.toString());
        }
        return new HashMap.SimpleEntry<>(sql.toString(), params.toMap());
    }

    @Override
    public FluentQuerySettingsHolder settingsHolder() {
        return this.settingsHolder;
    }

    private FluentQuery append(String sqlPart, ParameterMapConsumer<ParameterMap> params, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy) {
        List<String> strings = ValidationUtil.extractParameterKeys(sqlPart);
        List<PreparingQuery.ParamConditionPair> pairs = new LinkedList<>();
        for (String key : strings) {
            pairs.add(new PreparingQuery.ParamConditionPair(key, validationStrategy));
        }
        preparingQueries.add(new PreparingQuery(sqlPart, pairs));
        if (params != null) {
            params.accept(this.params);
        }
        return this;
    }

    @Override
    public FluentQuery append(String sqlPart) {
        return append(sqlPart, parameterMap -> {
        }, null);
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue) {
        return append(sqlPart, paramKey, paramValue, settingsHolder.getDefaultValidationStrategy());
    }

    @Override
    public FluentQuery append(String sqlPart, String paramKey, Object paramValue, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy) {
        return append(sqlPart, (params) -> params.addValue(null, paramKey, paramValue), validationStrategy);
    }

    @Override
    public FluentQuery append(String sqlPart, ParameterMapConsumer<ParameterMap> params) {
        return append(sqlPart, params, settingsHolder.getDefaultValidationStrategy());
    }

    @Override
    public FluentQuery param(String key, Object value) {
        return param(key, value, settingsHolder.getDefaultValidationStrategy());
    }

    @Override
    public FluentQuery param(Object beanPropertyParameterObj) {
        this.beanPropertyParameterObjs.add(beanPropertyParameterObj);
        return this;
    }

    @Override
    public FluentQuery param(String key, Object value, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy) {
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

    public abstract List<Object[]> listAsTuple();

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
            private Class<? extends ValidationStrategy<?, Boolean>> validationStrategy = null;

            protected ParamConditionPair() {

            }

            ParamConditionPair(String paramKey, Class<? extends ValidationStrategy<?, Boolean>> validationStrategy) {
                this.paramKey = paramKey;
                this.validationStrategy = validationStrategy;
            }

            public Class<? extends ValidationStrategy<?, Boolean>> getValidationStrategy() {
                return validationStrategy;
            }

            public String getParamKey() {
                return paramKey;
            }
        }
    }
}
