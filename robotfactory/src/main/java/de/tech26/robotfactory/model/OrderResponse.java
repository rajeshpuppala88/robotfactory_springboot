package de.tech26.robotfactory.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponse {

    @JsonProperty("order_id")
    private String orderId;
    private double total;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
