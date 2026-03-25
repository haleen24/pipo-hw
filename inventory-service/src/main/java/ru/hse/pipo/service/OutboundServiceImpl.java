package ru.hse.pipo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.exception.InventoryExceptionCode;
import ru.hse.pipo.mapper.OutboundShipmentMapper;
import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.model.OutboundShipmentStatus;
import ru.hse.pipo.model.Receiver;
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
    public OutboundShipment create(OutboundShipment outboundShipment) {
        Receiver receiver = receiverService.getByCode(outboundShipment.getReceiver().getCode());
        outboundShipment.setReceiver(receiver);
        outboundShipment.setStatus(OutboundShipmentStatus.IN_PROCESS.name());
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentMapper.toOutboundShipmentEntity(outboundShipment);
        OutboundShipmentEntity createdOutboundShipmentEntity = outboundShipmentRepository.save(outboundShipmentEntity);
        withdrawalService.create(createdOutboundShipmentEntity.getId(), outboundShipment.getWithdrawals());
        return outboundShipmentMapper.toOutboundShipment(createdOutboundShipmentEntity);
    }

    @Override
    public OutboundShipment get(Long id) {
        OutboundShipmentEntity outboundShipmentEntity = outboundShipmentRepository.findById(id).orElseThrow(() -> new InventoryException(
            InventoryExceptionCode.OUTBOUND_SHIPMENT_NOT_FOUND, id.toString()));
        return outboundShipmentMapper.toOutboundShipment(outboundShipmentEntity);
    }
}
