package ru.hse.pipo.service;

import java.util.List;

import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.model.Withdrawal;

public interface WithdrawalService {
    List<Withdrawal> create(OutboundShipment outboundShipment, List<Withdrawal> withdrawals);

    List<Withdrawal> getByOutboundShipmentId(Long outboundShipmentId);

    List<Withdrawal> fail(List<Withdrawal> withdrawals);
}
