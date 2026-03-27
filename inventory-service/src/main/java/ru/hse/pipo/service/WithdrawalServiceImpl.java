package ru.hse.pipo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.WithdrawalMapper;
import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.model.Withdrawal;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.repository.WithdrawalRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.NOT_ENOUGH_STOCK;
import static ru.hse.pipo.exception.InventoryExceptionCode.WITHDRAWAL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
    private final StockService stockService;
    private final WithdrawalRepository withdrawalRepository;
    private final WithdrawalMapper withdrawalMapper;
    private final ProductService productService;

    @Override
    public List<Withdrawal> create(OutboundShipment outboundShipment, List<Withdrawal> withdrawals) {
        Map<String, Long> productAmountByCode = withdrawals.stream()
            .collect(Collectors.toMap(withdrawal -> withdrawal.getProduct().getCode(), Withdrawal::getAmount, Long::sum));
        productAmountByCode.forEach((productCode, amount) -> {
            Long availableAmount = stockService.getStockCountByProductCode(productCode);
            if (availableAmount < amount) {
                throw new InventoryException(NOT_ENOUGH_STOCK, productCode, amount.toString(),
                    availableAmount.toString());
            }
        });
        List<WithdrawalEntity> withdrawalEntities = withdrawals.stream()
            .map(withdrawal -> {
                withdrawal.setOutboundShipment(outboundShipment);
                withdrawal.setStatus(WithdrawalStatus.CREATED);
                withdrawal.setProduct(productService.getByCode(withdrawal.getProduct().getCode()));
                return withdrawalMapper.toWithdrawalEntity(withdrawal);
            })
            .toList();
        return withdrawalMapper.toWithdrawals(withdrawalRepository.saveAllAndFlush(withdrawalEntities));
    }

    @Override
    public List<Withdrawal> getByOutboundShipmentId(Long outboundShipmentId) {
        return withdrawalRepository.findAllByOutboundShipmentId(outboundShipmentId).stream()
            .map(withdrawalMapper::toWithdrawal)
            .toList();
    }

    @Override
    public List<Withdrawal> fail(List<Withdrawal> withdrawals, String locationCodeForReturn) {
        withdrawals.stream()
            .filter(withdrawal -> withdrawal.getStatus() == WithdrawalStatus.COMPLETED)
            .forEach(withdrawal -> {
                stockService.moveToStock(locationCodeForReturn, withdrawal.getProduct().getCode(), withdrawal.getAmount(), null);
            });
        List<WithdrawalEntity> withdrawalEntities = withdrawals.stream()
            .map(withdrawal -> {
                withdrawal.setStatus(WithdrawalStatus.FAILED);
                return withdrawalMapper.toWithdrawalEntity(withdrawal);
            })
            .toList();
        return withdrawalMapper.toWithdrawals(withdrawalRepository.saveAllAndFlush(withdrawalEntities));
    }

    @Override
    public Withdrawal getById(Long id) {
        WithdrawalEntity withdrawalEntity =
            withdrawalRepository.findById(id).orElseThrow(() -> new InventoryException(WITHDRAWAL_NOT_FOUND, id.toString()));
        return withdrawalMapper.toWithdrawal(withdrawalEntity);
    }

    @Override
    public void update(Withdrawal withdrawal) {
        withdrawalRepository.save(withdrawalMapper.toWithdrawalEntity(withdrawal));
    }
}
