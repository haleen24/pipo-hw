package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.pipo.entity.OutboundShipmentEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal {
    private Long id;
    private String productCode;
    private Long amount;
    private OutboundShipmentEntity outboundShipmentEntity;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
