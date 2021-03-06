package io.github.firatgursoy.fluentquery.annotation;

import io.github.firatgursoy.fluentquery.validation.Validations;
import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import io.github.firatgursoy.fluentquery.validation.validations.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    boolean optional() default false;

    Validations using() default Validations.AUTO;
}
