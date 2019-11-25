package io.github.firatgursoy.fluentquery.jdbctemplate;

import io.github.firatgursoy.fluentquery.FluentQuery;
import io.github.firatgursoy.fluentquery.FluentQueryFactory;
import io.github.firatgursoy.fluentquery.validation.DefaultValidationRegistry;
import io.github.firatgursoy.fluentquery.validation.ValidationRegistry;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public class JdbcTemplateFluentQueryFactoryImpl implements FluentQueryFactory {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private ValidationRegistry validationRegistry = new DefaultValidationRegistry();

    public JdbcTemplateFluentQueryFactoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        if (jdbcTemplate == null) {
            throw new IllegalArgumentException("JdbcTemplate must not be null");
        }
        this.jdbcTemplate = jdbcTemplate;
    }


    public JdbcTemplateFluentQueryFactoryImpl(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("DataSource must not be null");
        }
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public FluentQuery newQuery() {
        return new JdbcTemplateFluentQuery(jdbcTemplate, validationRegistry);
    }

    @Override
    public ValidationRegistry validationRegistry() {
        return validationRegistry;
    }
}
