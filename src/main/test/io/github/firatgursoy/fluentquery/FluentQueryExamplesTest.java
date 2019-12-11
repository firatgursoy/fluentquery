package io.github.firatgursoy.fluentquery;

import io.github.firatgursoy.fluentquery.dto.Invoice;
import io.github.firatgursoy.fluentquery.dto.InvoiceDetail;
import io.github.firatgursoy.fluentquery.dto.InvoiceSearchForm;
import io.github.firatgursoy.fluentquery.jdbctemplate.JdbcTemplateFluentQueryFactoryImpl;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.List;

public abstract class FluentQueryExamplesTest {


    private static DataSource dataSource;
    private static MySQLContainer mysql;
    private static FluentQueryFactory factory;

    @BeforeAll
    public static void init() throws InterruptedException {
        //You can also use the GenericContainer for arbitrary containers
        //But there are convenient classes for common databases.
        mysql = new MySQLContainer("mysql:5.5.53");
        mysql.start();
        dataSource = DataSourceBuilder.create()
                .url(mysql.getJdbcUrl())
                .username(mysql.getUsername())
                .password(mysql.getPassword())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        factory = new JdbcTemplateFluentQueryFactoryImpl(dataSource);
        factory.settingsHolder().setDefaultSeparatorStrategy(SeparatorStrategy.SYSTEM)
                .setDefaultValidationStrategy(ValidationStrategy.AUTO);

        int integer = factory.newQuery().append("create table if not exists invoice (id int, code varchar(255), total_amount FLOAT(7,4));").update();
        int integer2 = factory.newQuery().append("create table if not exists invoice_detail (id int, invoice_id int, amount FLOAT(7,4), discount FLOAT(7,4));").update();
        factory.newQuery()
                .append("insert invoice (id, code,total_amount) values(1, :code, :totalAmount)",
                        parameterMap -> parameterMap.addValue("code", "abc").addValue(ValidationStrategy.NOT_ZERO_OR_NULL, "totalAmount", 750)
                )
                .update();
        factory.newQuery()
                .append("insert invoice_detail (id, invoice_id, amount, discount) values(1, 1, :amount, :discount)",
                        parameterMap -> parameterMap.addValue("amount", 250).addValue("discount", 221)
                )
                .update();
        factory.newQuery()
                .append("insert invoice_detail (id, invoice_id, amount, discount) values(1, 1, :amount, :discount)",
                        parameterMap -> parameterMap.addValue("amount", 500).addValue("discount", 111)
                )
                .update();


    }

    @AfterAll
    public static void destroy() {
        mysql.close();
    }

    @Test
    void example1() {
        FluentQuery fluentQuery = factory.newQuery()
                .append("select * from invoice where 1=1")
                .append("and code=:code")
                .param(new InvoiceSearchForm());
        List<Invoice> list = fluentQuery.list(Invoice.class);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void example2() {
        List<Invoice> list = factory.newQuery()
                .append("select i.id as invoiceId, i.total_amount as totalAmount, i.code as invoiceCode, idd.amount as invoiceDetailAmount, idd.discount as invoiceDetailDiscount from invoice i")
                .append("join invoice_detail idd on idd.invoice_id = i.id")
                .append("and i.id=:id", "id", 1, ValidationStrategy.NOT_NULL)
                .param(new InvoiceSearchForm())
                .listOneToMany(Invoice.class, InvoiceDetail.class, (invoice, invoiceDetail) -> invoice.getInvoiceDetails().add(invoiceDetail), "invoiceId");
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void example3() {
        List<Invoice> list = factory.newQuery()
                .append("select i.id as invoiceId, i.total_amount as totalAmount, i.code as invoiceCode, idd.amount as invoiceDetailAmount, idd.discount as invoiceDetailDiscount from invoice i")
                .append("join invoice_detail idd on idd.invoice_id = i.id")
                .append("and i.id=:id", "id", 1, ValidationStrategy.NOT_NULL)
                .append("and i.code=:code")
                .param(new InvoiceSearchForm())
                .listOneToMany(Invoice.class, InvoiceDetail.class, (invoice, invoiceDetail) -> invoice.getInvoiceDetails().add(invoiceDetail), "invoiceId");
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void example4() {
        List<Invoice> list = factory.newQuery()
                .append("select i.id as invoiceId, i.total_amount as totalAmount, i.code as invoiceCode, idd.amount as invoiceDetailAmount, idd.discount as invoiceDetailDiscount from invoice i")
                .append("join invoice_detail idd on idd.invoice_id = i.id")
                .append("and i.id=:id", "id", 1, ValidationStrategy.NOT_NULL)
                .append("and i.code=:code")
                .param(new InvoiceSearchForm())
                .listOneToMany(Invoice.class, InvoiceDetail.class, (invoice, invoiceDetail) -> invoice.getInvoiceDetails().add(invoiceDetail), "invoiceId");
        Assertions.assertEquals(1, list.size());
    }
}
