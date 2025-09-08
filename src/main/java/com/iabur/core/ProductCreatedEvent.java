package com.iabur.core;

public class ProductCreatedEvent {
    private String id;
    private String title;
    private Double price;
    private String description;

    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String productId, String title, Double price, String description) {
        this.id = productId;
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
