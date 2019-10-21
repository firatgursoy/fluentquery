package fluentquery.jdbctemplate;

import fluentquery.FluentQuery;
import fluentquery.FluentQueryFactory;
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
