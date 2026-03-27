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
import ru.hse.pipo.model.Withdrawal;
import ru.hse.pipo.repository.OutboundShipmentRepository;

@Service
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService {
    private final OutboundShipmentMapper outboundShipmentMapper;
    private final OutboundShipmentRepository outboundShipmentRepository;
    private final WithdrawalService withdrawalService;
    private final ReceiverService receiverService;

    @Override
    @Transactional
    public OutboundShipmentWithWithdrawals create(OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals) {
        OutboundShipment outboundShipment = outboundShipmentWithWithdrawals.getOutboundShipment();
        Receiver receiver = receiverService.getByCode(outboundShipment.getReceiver().getCode());
        outboundShipment.setReceiver(receiver);
        outboundShipment.setStatus(OutboundShipmentStatus.IN_PROCESS.name());
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
    public OutboundShipmentWithWithdrawals fail(Long id) {
        OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals = get(id);
        OutboundShipment outboundShipment = outboundShipmentWithWithdrawals.getOutboundShipment();
        outboundShipment.setStatus(OutboundShipmentStatus.CANCELED.name());
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentMapper.toOutboundShipmentEntity(outboundShipment);
        OutboundShipmentEntity updatedOutboundShipmentEntity = outboundShipmentRepository.save(outboundShipmentEntity);
        List<Withdrawal> withdrawals = withdrawalService.fail(outboundShipmentWithWithdrawals.getWithdrawals());
        return new OutboundShipmentWithWithdrawals(outboundShipmentMapper.toOutboundShipment(updatedOutboundShipmentEntity), withdrawals);
    }

    private OutboundShipment getById(Long id) {
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentRepository.findById(id)
            .orElseThrow(() -> new InventoryException(InventoryExceptionCode.OUTBOUND_SHIPMENT_NOT_FOUND, id.toString()));
        return outboundShipmentMapper.toOutboundShipment(outboundShipmentEntity);
    }
}
