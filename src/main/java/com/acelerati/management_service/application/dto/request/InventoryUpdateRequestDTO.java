package com.acelerati.management_service.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class InventoryUpdateRequestDTO {
    @NotNull
    @NotEmpty
    private final String name;
    @NotNull
    @Min(1)
    private final Long stock;
    @NotNull
    @Min(value = 100)
    private final BigDecimal unitPrice;
    @NotNull
    @Min(value = 100)
    private BigDecimal salePrice;
    @NotNull
    @Min(1)
    private final Long idProduct;
    @NotNull
    @Min(1)
    private final Long idSupplier;


}
