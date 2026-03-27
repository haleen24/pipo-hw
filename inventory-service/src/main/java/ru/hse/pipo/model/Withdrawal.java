package ru.hse.pipo.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal {
    private Long id;
    private Product product;
    private Long amount;
    private OutboundShipment outboundShipment;
    private WithdrawalStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
