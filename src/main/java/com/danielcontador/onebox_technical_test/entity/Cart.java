package com.danielcontador.onebox_technical_test.entity;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private final UUID id = UUID.randomUUID();
    private List<Product> products = new ArrayList<>();
    private Instant lastUpdate = Instant.now();
}
