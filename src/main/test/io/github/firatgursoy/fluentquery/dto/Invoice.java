package io.github.firatgursoy.fluentquery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Invoice {
    private Long invoiceId = 1L;
    private String invoiceCode;
    private BigDecimal totalAmount;
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();
}