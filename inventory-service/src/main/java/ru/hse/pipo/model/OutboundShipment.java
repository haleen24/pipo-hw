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
public class OutboundShipment {
    private Long id;
    private String externalShipmentId;
    private Receiver receiver;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}