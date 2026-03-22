package ru.hse.pipo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.inventory.controller.ShipmentApi;
import ru.hse.inventory.model.CreateShipmentRequest;
import ru.hse.inventory.model.CreateShipmentUnitRequest;
import ru.hse.inventory.model.ShipmentResponse;
import ru.hse.inventory.model.ShipmentUnitResponse;
import ru.hse.pipo.mapper.ShipmentMapper;
import ru.hse.pipo.mapper.ShipmentUnitMapper;
import ru.hse.pipo.model.Shipment;
import ru.hse.pipo.model.ShipmentUnit;
import ru.hse.pipo.service.ShipmentService;

@RestController
@RequiredArgsConstructor
public class ShipmentController implements ShipmentApi {
    private final ShipmentService shipmentService;
    private final ShipmentMapper shipmentMapper;
    private final ShipmentUnitMapper shipmentUnitMapper;

    @Override
    public ResponseEntity<ShipmentResponse> createShipment(CreateShipmentRequest createShipmentRequest) {
        Shipment shipment = shipmentMapper.toShipment(createShipmentRequest);
        Shipment createdShipment = shipmentService.create(shipment);
        ShipmentResponse shipmentResponse = shipmentMapper.toShipmentResponse(createdShipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentResponse);
    }

    @Override
    public ResponseEntity<ShipmentUnitResponse> createShipmentUnit(CreateShipmentUnitRequest createShipmentUnitRequest) {
        ShipmentUnit shipmentUnit = shipmentUnitMapper.toShipmentUnit(createShipmentUnitRequest);
        ShipmentUnit createdShipmentUnit = shipmentService.createShipmentUnit(shipmentUnit);
        ShipmentUnitResponse shipmentUnitResponse = shipmentUnitMapper.toShipmentUnitResponse(createdShipmentUnit);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentUnitResponse);
    }

    @Override
    public ResponseEntity<ShipmentResponse> getShipment(Long id) {
        Shipment shipment = shipmentService.getById(id);
        ShipmentResponse shipmentResponse = shipmentMapper.toShipmentResponse(shipment);
        return ResponseEntity.ok(shipmentResponse);
    }

    @Override
    public ResponseEntity<ShipmentUnitResponse> getShipmentUnit(Long id) {
        ShipmentUnit shipmentUnit = shipmentService.getShipmentUnitById(id);
        ShipmentUnitResponse shipmentUnitResponse = shipmentUnitMapper.toShipmentUnitResponse(shipmentUnit);
        return ResponseEntity.ok(shipmentUnitResponse);
    }
}
