package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.jdbctemplate.JdbcTemplateFluentQueryFactoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class JdbcTemplateFluentQueryFactoryTest extends FluentQueryExamplesTest {
    @Test
    public void exceptIllegalArgumentExceptionOnNullDataSourceConstructor() {
        DataSource dataSource = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> new JdbcTemplateFluentQueryFactoryImpl(dataSource));
    }
}
