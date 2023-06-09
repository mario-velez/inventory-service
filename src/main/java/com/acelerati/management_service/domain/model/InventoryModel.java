package com.acelerati.management_service.domain.model;

import java.math.BigDecimal;

public class InventoryModel {
    private Long idInventory;
    private String name;
    private Long stock;
    private BigDecimal unitPrice;
    private BigDecimal salePrice;
    private Long idProduct;
    private Long idSupplier;
    

    public InventoryModel() {
    }


    public InventoryModel(Long idInventory, String name, Long stock, BigDecimal unitPrice, BigDecimal salePrice, Long idProduct, Long idSupplier) {
        this.idInventory = idInventory;
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.salePrice = salePrice;
        this.idProduct = idProduct;
        this.idSupplier = idSupplier;
    }

    public Long getIdInventory() {
        return idInventory;
    }

    public void setIdInventory(Long idInventory) {
        this.idInventory = idInventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Long getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Long idSupplier) {
        this.idSupplier = idSupplier;
    }
}
