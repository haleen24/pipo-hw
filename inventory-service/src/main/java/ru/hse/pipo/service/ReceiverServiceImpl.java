package ru.hse.pipo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ReceiverEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.ReceiverMapper;
import ru.hse.pipo.model.Receiver;
import ru.hse.pipo.repository.ReceiverRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.RECEIVER_CODE_IS_NOT_UNIQUE;
import static ru.hse.pipo.exception.InventoryExceptionCode.RECEIVER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReceiverServiceImpl implements ReceiverService {
    private final ReceiverMapper receiverMapper;
    private final ReceiverRepository receiverRepository;

    @Override
    public Receiver create(Receiver receiver) {
        if (receiverRepository.findByCode(receiver.getCode()) != null) {
            throw new InventoryException(RECEIVER_CODE_IS_NOT_UNIQUE, receiver.getCode());
        }
        ReceiverEntity receiverEntity = receiverMapper.toReceiverEntity(receiver);
        ReceiverEntity createdReceiverEntity = receiverRepository.save(receiverEntity);
        return receiverMapper.toReceiver(createdReceiverEntity);
    }

    @Override
    public Receiver getByCode(String code) {
        ReceiverEntity receiverEntity = receiverRepository.findByCode(code);
        if (receiverEntity == null) {
            throw new InventoryException(RECEIVER_NOT_FOUND, code);
        }
        return receiverMapper.toReceiver(receiverEntity);
    }
}
