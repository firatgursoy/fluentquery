package io.github.firatgursoy.fluentquery.jdbctemplate;

import io.github.firatgursoy.fluentquery.AbstractFluentQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;

/**
 * FluentQuery is a sql query builder by using java 8 functional interfaces and spring's JdbcTemplate.
 * Main use-case is complex conditional query building situations.
 * Benefits are high code readability, easy validation and easy to integrate sql to java code.
 *
 * @author firat.gursoy
 */
public class JdbcTemplateFluentQuery extends AbstractFluentQuery {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private JdbcTemplateFluentQuery() {
    }

    JdbcTemplateFluentQuery(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private <T> List<T> list(RowMapper<T> rowMapper) {
        return jdbcTemplate.query(getSql().toString(), getParams().getValues(), rowMapper);
    }

    @Override
    public String toSql() {
        return getSql().toString();
    }

    @Override
    public <T> List<T> list(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass));
    }

    @Override
    public <T> Optional<T> getOptional(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass)).stream().findFirst();
    }

    @Override
    public <T> T get(Class<T> mappedClass) {
        return list(new BeanPropertyRowMapper<>(mappedClass)).stream().findFirst().get();
    }
}
