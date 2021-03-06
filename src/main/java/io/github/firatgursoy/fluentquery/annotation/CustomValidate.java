package io.github.firatgursoy.fluentquery.annotation;

import io.github.firatgursoy.fluentquery.validation.ValidationStrategy;
import io.github.firatgursoy.fluentquery.validation.validations.AutoValidationStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface CustomValidate {

    Class<? extends ValidationStrategy<?, Boolean>> usingClass() default AutoValidationStrategy.class;

}
