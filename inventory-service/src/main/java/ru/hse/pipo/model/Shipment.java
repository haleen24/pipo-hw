package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    private Long id;
    private Supplier supplier;
    private String externalShipmentId;
    private Product product;
    private ShipmentStatus status;
    private Long shipmentUnitCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
