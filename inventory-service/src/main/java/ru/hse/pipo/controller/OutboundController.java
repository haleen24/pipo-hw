package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.OutboundApi;
import ru.hse.inventory.model.CreateOutboundShipmentRequest;
import ru.hse.inventory.model.OutboundShipmentResponse;
import ru.hse.pipo.mapper.OutboundShipmentMapper;
import ru.hse.pipo.model.OutboundShipmentWithWithdrawals;
import ru.hse.pipo.service.OutboundService;

@RestController
@RequiredArgsConstructor
public class OutboundController implements OutboundApi {
    private final OutboundService outboundService;
    private final OutboundShipmentMapper outboundShipmentMapper;

    @Override
    public ResponseEntity<OutboundShipmentResponse> createOutboundShipment(CreateOutboundShipmentRequest createOutboundShipmentRequest) {
        OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals =
            outboundShipmentMapper.toOutboundShipment(createOutboundShipmentRequest);
        OutboundShipmentWithWithdrawals createdoutboundShipmentWithWithdrawals = outboundService.create(outboundShipmentWithWithdrawals);
        OutboundShipmentResponse outboundShipmentResponse =
            outboundShipmentMapper.toOutboundShipmentResponse(createdoutboundShipmentWithWithdrawals.getOutboundShipment(),
                createdoutboundShipmentWithWithdrawals.getWithdrawals());
        return ResponseEntity.status(HttpStatus.CREATED).body(outboundShipmentResponse);
    }

    @Override
    public ResponseEntity<OutboundShipmentResponse> failOutboundShipment(Long id) {
        OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals = outboundService.fail(id);
        OutboundShipmentResponse outboundShipmentResponse =
            outboundShipmentMapper.toOutboundShipmentResponse(outboundShipmentWithWithdrawals.getOutboundShipment(),
                outboundShipmentWithWithdrawals.getWithdrawals());
        return ResponseEntity.ok(outboundShipmentResponse);
    }

    @Override
    public ResponseEntity<OutboundShipmentResponse> getOutboundShipment(Long id) {
        OutboundShipmentWithWithdrawals outboundShipmentWithWithdrawals = outboundService.get(id);
        OutboundShipmentResponse outboundShipmentResponse =
            outboundShipmentMapper.toOutboundShipmentResponse(outboundShipmentWithWithdrawals.getOutboundShipment(),
                outboundShipmentWithWithdrawals.getWithdrawals());
        return ResponseEntity.ok(outboundShipmentResponse);
    }
}
