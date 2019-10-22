package io.github.firatgursoy.fluentquery.jdbctemplate;

import io.github.firatgursoy.fluentquery.FluentQuery;
import io.github.firatgursoy.fluentquery.FluentQueryFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcTemplateFluentQueryFactoryImpl implements FluentQueryFactory {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateFluentQueryFactoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FluentQuery newQuery() {
        return new JdbcTemplateFluentQuery(jdbcTemplate);
    }
}
