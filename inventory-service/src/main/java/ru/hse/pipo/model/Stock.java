package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {
    private Long id;
    private Location location;
    private Product product;
    private Long amount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
