package com.danielcontador.onebox_technical_test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private final long id = generateId();
    private static long counter = 0;
    private String description;
    private int amount;

    private static long generateId() {
        return ++counter;
    }
}
