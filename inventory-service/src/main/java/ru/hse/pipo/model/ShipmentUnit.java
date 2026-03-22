package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentUnit {
    private Long id;
    private Shipment shipment;
    private Long amount;
    private Integer lengthCm;
    private Integer widthCm;
    private Integer heightCm;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
