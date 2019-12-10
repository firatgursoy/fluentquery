package io.github.firatgursoy.fluentquery.dto;

import io.github.firatgursoy.fluentquery.annotation.Validate;
import io.github.firatgursoy.fluentquery.validation.Validations;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceSearchForm {
    @Validate(mustBeValidated = false, using = Validations.AUTO)
    private String code = "";
}