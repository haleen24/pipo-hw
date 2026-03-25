package ru.hse.pipo.service;

import java.util.List;

import ru.hse.pipo.model.Withdrawal;

public interface WithdrawalService {
    List<Withdrawal> create(Long outboundShipmentId, List<Withdrawal> withdrawals);
}
