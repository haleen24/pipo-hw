package ru.hse.pipo.service;

import ru.hse.inventory.model.ProcessWithdrawalRequest;
import ru.hse.pipo.model.OutboundShipmentWithWithdrawals;
import ru.hse.pipo.model.Withdrawal;

public interface OutboundService {
    OutboundShipmentWithWithdrawals create(OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals);

    OutboundShipmentWithWithdrawals get(Long id);

    OutboundShipmentWithWithdrawals fail(Long id, String locationCodeForReturn);

    Withdrawal processWithdrawal(Long withdrawalId, String locationCode);
}
