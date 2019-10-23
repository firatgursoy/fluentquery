# fluentquery
FluentQuery is a flexible SQL query builder which utilizes JAVA 8 Functional Interfaces and Spring's JdbcTemplate.

 Main use-case is complex conditional query building situations.
 Benefits are high code readability, easy validation and easy to integrate sql to java code.



```xml
<dependency>
  <groupId>io.github.firatgursoy</groupId>
  <artifactId>fluentquery</artifactId>
  <version>0.0.1</version>
</dependency>
```

```java
@Configuration
public SpringConfiguration {

  @Bean
  FluentQueryFactory queryFactory(NamedParameterJdbcTemplate jdbcTemplate){
    return new JdbcTemplateFluentQueryFactoryImpl(jdbcTemplate);
  }
  
}

@Repository
public SampleUsageRepository {

  @Autowired
  FluentQueryFactory queryFactory;
  
  public List<SampleDto> getSampleDtos(){
    return queryFactory.newQuery()
    .append("select * from sample_dto")
    .list(SampleDto.class);
  }
  
}
```
