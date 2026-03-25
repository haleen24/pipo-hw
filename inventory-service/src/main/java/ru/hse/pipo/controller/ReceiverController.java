package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.ReceiverApi;
import ru.hse.inventory.model.CreateReceiverRequest;
import ru.hse.inventory.model.ReceiverResponse;
import ru.hse.pipo.mapper.ReceiverMapper;
import ru.hse.pipo.model.Receiver;
import ru.hse.pipo.service.ReceiverService;

@RestController
@RequiredArgsConstructor
public class ReceiverController implements ReceiverApi {
    private final ReceiverService receiverService;
    private final ReceiverMapper receiverMapper;

    @Override
    public ResponseEntity<ReceiverResponse> createReceiver(CreateReceiverRequest createReceiverRequest) {
        Receiver receiver = receiverMapper.toReceiver(createReceiverRequest);
        Receiver createdReceiver = receiverService.create(receiver);
        ReceiverResponse receiverResponse = receiverMapper.toReceiverResponse(createdReceiver);
        return ResponseEntity.status(HttpStatus.CREATED).body(receiverResponse);
    }

    @Override
    public ResponseEntity<ReceiverResponse> getReceiver(String code) {
        Receiver receiver = receiverService.getByCode(code);
        ReceiverResponse receiverResponse = receiverMapper.toReceiverResponse(receiver);
        return ResponseEntity.ok(receiverResponse);
    }
}
