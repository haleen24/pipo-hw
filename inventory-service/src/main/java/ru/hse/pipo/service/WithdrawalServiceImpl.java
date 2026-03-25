package ru.hse.pipo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.WithdrawalMapper;
import ru.hse.pipo.model.Withdrawal;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.repository.WithdrawalRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.NOT_ENOUGH_STOCK;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final StockService stockService;
    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalMapper withdrawalMapper;

    @Override
    public List<Withdrawal> create(Long outboundShipmentId, List<Withdrawal> withdrawals) {
        Map<String, Long> productAmountById = withdrawals.stream()
            .collect(Collectors.toMap(Withdrawal::getProductCode, Withdrawal::getAmount, Long::sum));
        productAmountById.forEach((productCode, amount) -> {
            Long availableAmount = stockService.getStockCountByProductCode(productCode);
            if (availableAmount < amount) {
                throw new InventoryException(NOT_ENOUGH_STOCK, productCode, amount.toString(),
                    availableAmount.toString());
            }
        });
        List<WithdrawalEntity> withdrawalEntities = withdrawals.stream()
            .map(withdrawal -> {
                withdrawal.setOutboundShipmentEntity(OutboundShipmentEntity.builder().id(outboundShipmentId).build());
                withdrawal.setStatus(WithdrawalStatus.CREATED.name());
                return withdrawalMapper.toWithdrawalEntity(withdrawal);
            })
            .toList();
        return withdrawalMapper.toWithdrawals(withdrawalRepository.saveAll(withdrawalEntities));
    }
}
