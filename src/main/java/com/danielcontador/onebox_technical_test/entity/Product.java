package com.danielcontador.onebox_technical_test.entity;

import lombok.Data;

@Data
public class Product {
    private long id;
    private static long counter = 0;
    private String description;
    private int amount;

    public Product(String description, int amount) {
        this.id = generateId();
        this.description = description;
        this.amount = amount;
    }

    private long generateId() {
        return ++counter;
    }
}
