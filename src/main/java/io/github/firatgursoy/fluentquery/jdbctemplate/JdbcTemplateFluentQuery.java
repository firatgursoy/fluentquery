package io.github.firatgursoy.fluentquery.jdbctemplate;

import io.github.firatgursoy.fluentquery.AbstractFluentQuery;
import io.github.firatgursoy.fluentquery.FluentQuerySettingsHolder;
import io.github.firatgursoy.fluentquery.annotation.Validate;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import io.github.firatgursoy.fluentquery.validation.Validations;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public class JdbcTemplateFluentQuery extends AbstractFluentQuery {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateFluentQuery(NamedParameterJdbcTemplate jdbcTemplate, FluentQuerySettingsHolder settingsHolder) {
        super(settingsHolder);
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("JdbcTemplate must not be null");
        }
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> list(RowMapper<T> rowMapper) {
        Map.Entry<String, Map<String, Object>> validatedQuery = prepare();
        return jdbcTemplate.query(validatedQuery.getKey(), validatedQuery.getValue(), rowMapper);
    }

    @Override
    public List<Object[]> listAsTuple() {
        return list((rs, rowNum) -> {
            List<Object> columns = new ArrayList<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                columns.add(rs.getObject(i));
            }
            return columns.toArray();
        });
    }
    
    @Override
    public int update() {
        fetchBeanPropertyParameterSource();
        Map.Entry<String, Map<String, Object>> validatedQuery = prepare();
        return jdbcTemplate.update(validatedQuery.getKey(), validatedQuery.getValue());
    }

    @Override
    public <T> List<T> list(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass));
    }

    @Override
    public <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addSubClassBiConsumer) {
        return listOneToMany(mappedClass, subClass, addSubClassBiConsumer, "id");
    }

    @Override
    public <T, S> List<T> listOneToMany(Class<T> mappedClass, Class<S> subClass, BiConsumer<T, S> addSubClassBiConsumer, String idColumnLabel) {
        Map.Entry<String, Map<String, Object>> validatedQuery = prepare();
        return jdbcTemplate.query(validatedQuery.getKey(), validatedQuery.getValue(),
                new OneToManyResultSetExtractor<T, S, Long>(
                        new BeanPropertyRowMapper<>(mappedClass),
                        new BeanPropertyRowMapper<>(subClass)
                ) {
                    @Override
                    protected Long mapPrimaryKey(ResultSet rs) throws SQLException {
                        return rs.getLong(idColumnLabel);
                    }

                    @Override
                    protected Long mapForeignKey(ResultSet rs) throws SQLException {
                        if (rs.getObject(idColumnLabel) == null) {
                            return null;
                        } else {
                            return rs.getLong(idColumnLabel);
                        }
                    }

                    @Override
                    protected void addChild(T root, S child) {
                        addSubClassBiConsumer.accept(root, child);
                    }
                });
    }

    @Override
    public <T> Optional<T> getOptional(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass)).stream().findFirst();
    }

    @Override
    public <T> T get(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass)).stream().findFirst().get();
    }

    @Override
    public void fetchBeanPropertyParameterSource() {
        for (Object beanPropertyParameterObj : beanPropertyParameterObjs) {
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(beanPropertyParameterObj);
            BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(beanPropertyParameterObj);
            String[] parameterNames = beanPropertySqlParameterSource.getReadablePropertyNames();
            if (parameterNames != null) {
                parameterNames = Arrays.stream(parameterNames).filter(param -> !param.equalsIgnoreCase("class")).toArray(String[]::new);
                for (String parameterName : parameterNames) {
                    TypeDescriptor propertyTypeDescriptor = bw.getPropertyTypeDescriptor(parameterName);
                    Class<? extends ValidationStrategy<?, Boolean>> validationClass = settingsHolder().getDefaultValidationStrategy();
                    if (propertyTypeDescriptor != null) {
                        if (propertyTypeDescriptor.hasAnnotation(Validate.class)) {
                            Validate annotation = propertyTypeDescriptor.getAnnotation(Validate.class);
                            if (!annotation.optional()) {
                                validationClass = annotation.using().getValidationClass();
                            } else {
                                validationClass = settingsHolder().getDefaultValidationStrategy();
                            }
                        } else {
                            validationClass = Validations.NONE.getValidationClass();
                        }
                    }
                    params.addValue(validationClass, parameterName, beanPropertySqlParameterSource.getValue(parameterName));
                }
            }
        }
    }
}
