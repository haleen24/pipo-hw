package ru.hse.pipo.model;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundShipment {
    private Long id;
    private String externalShipmentId;
    private Receiver receiver;
    private String status;
    private List<Withdrawal> withdrawals;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}