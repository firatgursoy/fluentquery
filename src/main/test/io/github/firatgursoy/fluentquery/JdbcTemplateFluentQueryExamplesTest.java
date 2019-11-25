package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.jdbctemplate.JdbcTemplateFluentQuery;
import io.github.firatgursoy.fluentquery.validation.DefaultValidationRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcTemplateFluentQueryExamplesTest {

    @Test
    public void exceptIllegalArgumentExceptionOnNullNamedParameterJdbcTemplateConstructor() {
        NamedParameterJdbcTemplate jdbcTemplate = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new JdbcTemplateFluentQuery(jdbcTemplate, new DefaultValidationRegistry()));
    }
}