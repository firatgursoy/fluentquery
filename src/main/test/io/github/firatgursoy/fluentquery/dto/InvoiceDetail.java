package io.github.firatgursoy.fluentquery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class InvoiceDetail {
    private BigDecimal invoiceDetailAmount;
    private BigDecimal invoiceDetailDiscount;
}
