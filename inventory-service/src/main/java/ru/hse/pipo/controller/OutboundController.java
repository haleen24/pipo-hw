package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.OutboundApi;
import ru.hse.inventory.model.CreateOutboundShipmentRequest;
import ru.hse.inventory.model.OutboundShipmentResponse;
import ru.hse.pipo.mapper.OutboundShipmentMapper;
import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.service.OutboundService;

@RestController
@RequiredArgsConstructor
public class OutboundController implements OutboundApi {
    private final OutboundService outboundService;
    private final OutboundShipmentMapper outboundShipmentMapper;

    @Override
    public ResponseEntity<OutboundShipmentResponse> createOutboundShipment(CreateOutboundShipmentRequest createOutboundShipmentRequest) {
        OutboundShipment outboundShipment = outboundShipmentMapper.toOutboundShipment(createOutboundShipmentRequest);
        OutboundShipment createdOutboundShipment = outboundService.create(outboundShipment);
        OutboundShipmentResponse outboundShipmentResponse = outboundShipmentMapper.toOutboundShipmentResponse(createdOutboundShipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(outboundShipmentResponse);
    }

    @Override
    public ResponseEntity<OutboundShipmentResponse> failOutboundShipment(Long id) {
        OutboundShipment outboundShipment = outboundService.fail(id);
        OutboundShipmentResponse outboundShipmentResponse = outboundShipmentMapper.toOutboundShipmentResponse(outboundShipment);
        return ResponseEntity.ok(outboundShipmentResponse);
    }

    @Override
    public ResponseEntity<OutboundShipmentResponse> getOutboundShipment(Long id) {
        OutboundShipment outboundShipment = outboundService.get(id);
        OutboundShipmentResponse outboundShipmentResponse = outboundShipmentMapper.toOutboundShipmentResponse(outboundShipment);
        return ResponseEntity.ok(outboundShipmentResponse);
    }
}
