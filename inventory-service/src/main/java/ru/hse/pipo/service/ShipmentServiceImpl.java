package ru.hse.pipo.service;

import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.exception.InventoryException;
import ru.hse.pipo.mapper.ShipmentMapper;
import ru.hse.pipo.model.Product;
import ru.hse.pipo.model.Shipment;
import ru.hse.pipo.model.ShipmentStatus;
import ru.hse.pipo.model.ShipmentUnit;
import ru.hse.pipo.model.Supplier;
import ru.hse.pipo.repository.ShipmentRepository;

import static ru.hse.pipo.exception.InventoryExceptionCode.SHIPMENT_NOT_FOUND;
import static ru.hse.pipo.model.ShipmentStatus.CREATED;
import static ru.hse.pipo.model.ShipmentStatus.IN_PROCESS;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final ProductService productService;
    private final SupplierService supplierService;
    private final ShipmentUnitService shipmentUnitService;

    @Override
    @Transactional
    public Shipment create(Shipment shipment) {
        Product product = productService.getByCode(shipment.getProduct().getCode());
        shipment.setProduct(product);
        Supplier supplier = supplierService.getByCode(shipment.getSupplier().getCode());
        shipment.setSupplier(supplier);
        shipment.setStatus(CREATED);
        ShipmentEntity shipmentEntity = shipmentMapper.toShipmentEntity(shipment);
        ShipmentEntity createdShipmentEntity = shipmentRepository.save(shipmentEntity);
        return shipmentMapper.toShipment(createdShipmentEntity);
    }

    @Override
    public Shipment getById(Long id) {
        ShipmentEntity shipmentEntity = getShipmentEntity(id);
        return shipmentMapper.toShipment(shipmentEntity);
    }

    @Override
    @Transactional
    public ShipmentUnit createShipmentUnit(ShipmentUnit shipmentUnit) {
        ShipmentEntity shipmentEntity = getShipmentEntity(shipmentUnit.getShipment().getId());
        if (shipmentEntity.getStatus().equals(CREATED.name())) {
            shipmentEntity.setStatus(IN_PROCESS.name());
            shipmentRepository.save(shipmentEntity);
        }
        Shipment shipment = shipmentMapper.toShipment(shipmentEntity);
        shipmentUnit.setShipment(shipment);
        return shipmentUnitService.create(shipmentUnit);
    }

    @Override
    public ShipmentUnit getShipmentUnitById(Long id) {
        return shipmentUnitService.getById(id);
    }

    @Override
    @Transactional
    public ShipmentUnit moveShipmentUnit(Long shipmentUnitId, String locationCode) {
        ShipmentUnit shipmentUnit = shipmentUnitService.moveById(shipmentUnitId, locationCode);
        Shipment shipment = shipmentUnit.getShipment();
        Boolean shipmentComplete = shipmentUnitService.isShipmentComplete(shipment);
        if (!shipmentComplete) {
            return shipmentUnit;
        }
        shipment.setStatus(ShipmentStatus.COMPLETE);
        ShipmentEntity shipmentEntity = shipmentMapper.toShipmentEntity(shipment);
        shipmentRepository.save(shipmentEntity);
        return getShipmentUnitById(shipmentUnitId);
    }

    private ShipmentEntity getShipmentEntity(Long id) {
        Optional<ShipmentEntity> shipmentEntity = shipmentRepository.findById(id);
        if (shipmentEntity.isEmpty()) {
            throw new InventoryException(SHIPMENT_NOT_FOUND, id.toString());
        }
        return shipmentEntity.get();
    }
}
