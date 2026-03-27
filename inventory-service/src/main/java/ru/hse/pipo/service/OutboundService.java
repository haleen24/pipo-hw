package ru.hse.pipo.service;

import ru.hse.pipo.model.OutboundShipmentWithWithdrawals;

public interface OutboundService {
    OutboundShipmentWithWithdrawals create(OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals);

    OutboundShipmentWithWithdrawals get(Long id);

    OutboundShipmentWithWithdrawals fail(Long id);
}
