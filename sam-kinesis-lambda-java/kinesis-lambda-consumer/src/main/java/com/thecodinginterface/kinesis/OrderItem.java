package com.thecodinginterface.kinesis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("product_quantity")
    private int productQuantity;

    @JsonProperty("product_price")
    private double productPrice;

    public OrderItem() {}

    public OrderItem(String productName, String productCode, int productQuantity, double productPrice) {
        this.productName = productName;
        this.productCode = productCode;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return String.format("OrderItem{productName='%s', productCode='%s', productQuantity=%d, productPrice=%.2f}",
                productName, productCode, productQuantity, productPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (productQuantity != orderItem.productQuantity) return false;
        if (Double.compare(orderItem.productPrice, productPrice) != 0) return false;
        if (!productName.equals(orderItem.productName)) return false;
        return productCode.equals(orderItem.productCode);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = productName.hashCode();
        result = 31 * result + productCode.hashCode();
        result = 31 * result + productQuantity;
        temp = Double.doubleToLongBits(productPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
