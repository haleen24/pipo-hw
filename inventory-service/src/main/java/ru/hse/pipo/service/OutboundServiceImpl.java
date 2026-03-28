package ru.hse.pipo.service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.OutboundShipmentMapper;
import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.model.OutboundShipmentStatus;
import ru.hse.pipo.model.OutboundShipmentWithWithdrawals;
import ru.hse.pipo.model.Receiver;
import ru.hse.pipo.model.Stock;
import ru.hse.pipo.model.Withdrawal;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.repository.OutboundShipmentRepository;

@Service
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService {
    private final OutboundShipmentMapper outboundShipmentMapper;
    private final OutboundShipmentRepository outboundShipmentRepository;
    private final WithdrawalService withdrawalService;
    private final ReceiverService receiverService;
    private final StockService stockService;

    @Override
    @Transactional
    public OutboundShipmentWithWithdrawals create(OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals) {
        OutboundShipment outboundShipment = outboundShipmentWithWithdrawals.getOutboundShipment();
        Receiver receiver = receiverService.getByCode(outboundShipment.getReceiver().getCode());
        outboundShipment.setReceiver(receiver);
        outboundShipment.setStatus(OutboundShipmentStatus.IN_PROCESS);
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentMapper.toOutboundShipmentEntity(outboundShipment);
        OutboundShipmentEntity createdOutboundShipmentEntity = outboundShipmentRepository.save(outboundShipmentEntity);
        OutboundShipment createdOutboundShipment = outboundShipmentMapper.toOutboundShipment(createdOutboundShipmentEntity);
        List<Withdrawal> withdrawals = withdrawalService.create(createdOutboundShipment, outboundShipmentWithWithdrawals.getWithdrawals());
        return new OutboundShipmentWithWithdrawals(createdOutboundShipment, withdrawals);
    }

    @Override
    public OutboundShipmentWithWithdrawals get(Long id) {
        List<Withdrawal> withdrawals = withdrawalService.getByOutboundShipmentId(id);
        OutboundShipment outboundShipment = withdrawals.isEmpty() ? getById(id) : withdrawals.getFirst().getOutboundShipment();
        return new OutboundShipmentWithWithdrawals(outboundShipment, withdrawals);
    }

    @Override
    public OutboundShipmentWithWithdrawals fail(Long id, String locationCodeForReturn) {
        OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals = get(id);
        OutboundShipment outboundShipment = outboundShipmentWithWithdrawals.getOutboundShipment();
        outboundShipment.setStatus(OutboundShipmentStatus.CANCELED);
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentMapper.toOutboundShipmentEntity(outboundShipment);
        OutboundShipmentEntity updatedOutboundShipmentEntity = outboundShipmentRepository.save(outboundShipmentEntity);
        List<Withdrawal> withdrawals = withdrawalService.fail(outboundShipmentWithWithdrawals.getWithdrawals(), locationCodeForReturn);
        return new OutboundShipmentWithWithdrawals(outboundShipmentMapper.toOutboundShipment(updatedOutboundShipmentEntity), withdrawals);
    }

    @Override
    @Transactional
    public Withdrawal processWithdrawal(Long withdrawalId, String locationCode) {
        Withdrawal withdrawal = withdrawalService.getById(withdrawalId);
        OutboundShipment outboundShipment = withdrawal.getOutboundShipment();
        if (outboundShipment.getStatus() == OutboundShipmentStatus.CANCELED) {
            throw new InventoryException(InventoryExceptionCode.OUTBOUND_SHIPMENT_CANCELED, withdrawalId.toString());
        }
        List<Stock> stocksInLocation = stockService.getStockByLocationCode(locationCode);
        String productCode = withdrawal.getProduct().getCode();
        Stock stock = stocksInLocation.stream()
            .filter(s -> s.getProduct().getCode().equals(productCode))
            .findFirst().orElseThrow(
                () -> new InventoryException(InventoryExceptionCode.STOCK_NOT_FOUND, productCode, locationCode));
        if (stock.getAmount() < withdrawal.getAmount()) {
            throw new InventoryException(InventoryExceptionCode.NOT_ENOUGH_STOCK, productCode, withdrawal.getAmount().toString(),
                stock.getAmount().toString());
        }
        stock.setAmount(stock.getAmount() - withdrawal.getAmount());
        stockService.update(stock);
        withdrawal.setStatus(WithdrawalStatus.COMPLETED);
        withdrawalService.update(withdrawal);
        checkCompleteOutboundShipment(outboundShipment);
        return withdrawal;
    }

    private OutboundShipment getById(Long id) {
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentRepository.findById(id)
            .orElseThrow(() -> new InventoryException(InventoryExceptionCode.OUTBOUND_SHIPMENT_NOT_FOUND, id.toString()));
        return outboundShipmentMapper.toOutboundShipment(outboundShipmentEntity);
    }

    private void checkCompleteOutboundShipment(OutboundShipment outboundShipment) {
        List<Withdrawal> withdrawals = withdrawalService.getByOutboundShipmentId(outboundShipment.getId());
        if (withdrawals.stream().allMatch(w -> w.getStatus() == WithdrawalStatus.COMPLETED)) {
            outboundShipment.setStatus(OutboundShipmentStatus.COMPLETED);
            OutboundShipmentEntity outboundShipmentEntity = outboundShipmentMapper.toOutboundShipmentEntity(outboundShipment);
            outboundShipmentRepository.save(outboundShipmentEntity);
        }
    }
}
