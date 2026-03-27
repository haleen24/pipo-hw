package ru.hse.pipo.model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboundShipmentWithWithdrawals {
    private OutboundShipment outboundShipment;
    private List<Withdrawal> withdrawals;
}
