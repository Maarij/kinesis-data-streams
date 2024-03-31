package com.thecodinginterface.kinesis;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order {
    @JsonProperty("order_items")
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonProperty("customer_id")
    private String customerID;

    @JsonProperty("order_id")
    private String orderID;

    @JsonProperty("seller_id")
    private String sellerID;

    public Order() {}

    public Order(List<OrderItem> orderItems, String customerID, String orderID, String sellerID) {
        this.orderItems = orderItems;
        this.customerID = customerID;
        this.orderID = orderID;
        this.sellerID = sellerID;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    @Override
    public String toString() {
        return String.format("Order{orderID='%s', customerID='%s', sellerID='%s', orderItems=%s}",
                orderID, customerID, sellerID, Arrays.toString(orderItems.toArray()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!orderItems.equals(order.orderItems)) return false;
        if (!customerID.equals(order.customerID)) return false;
        if (!orderID.equals(order.orderID)) return false;
        return sellerID.equals(order.sellerID);
    }

    @Override
    public int hashCode() {
        int result = orderItems.hashCode();
        result = 31 * result + customerID.hashCode();
        result = 31 * result + orderID.hashCode();
        result = 31 * result + sellerID.hashCode();
        return result;
    }
}
