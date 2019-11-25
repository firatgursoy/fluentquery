package io.github.firatgursoy.fluentquery.jdbctemplate;

import io.github.firatgursoy.fluentquery.AbstractFluentQuery;
import io.github.firatgursoy.fluentquery.validation.ValidationRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public class JdbcTemplateFluentQuery extends AbstractFluentQuery {

    protected final Log logger = LogFactory.getLog(getClass());
    private NamedParameterJdbcTemplate jdbcTemplate;


    public JdbcTemplateFluentQuery(NamedParameterJdbcTemplate jdbcTemplate, ValidationRegistry validationRegistry) {
        super(validationRegistry);
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
            BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(beanPropertyParameterObj);
            String[] parameterNames = beanPropertySqlParameterSource.getParameterNames();
            if (parameterNames != null) {
                parameterNames = Arrays.stream(parameterNames).filter(param -> !param.equalsIgnoreCase("class")).toArray(String[]::new);
                for (String parameterName : parameterNames) {
                    params.addValue(defaultValidationStrategy, parameterName, beanPropertySqlParameterSource.getValue(parameterName));
                }
            }
        }
    }
}
