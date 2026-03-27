package ru.hse.pipo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.entity.ReceiverEntity;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.entity.ShipmentUnitEntity;
import ru.hse.pipo.entity.StockEntity;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.model.ShipmentStatus;
import ru.hse.pipo.model.WithdrawalStatus;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.LocationRepository;
import ru.hse.pipo.repository.LocationTypeRepository;
import ru.hse.pipo.repository.OutboundShipmentRepository;
import ru.hse.pipo.repository.ProductRepository;
import ru.hse.pipo.repository.ReceiverRepository;
import ru.hse.pipo.repository.ShipmentRepository;
import ru.hse.pipo.repository.ShipmentUnitRepository;
import ru.hse.pipo.repository.StockRepository;
import ru.hse.pipo.repository.SupplierRepository;
import ru.hse.pipo.repository.WithdrawalRepository;
import ru.hse.pipo.repository.ZoneRepository;

import static ru.hse.pipo.CommonTestConfiguration.generateLong;
import static ru.hse.pipo.CommonTestConfiguration.generateString;


@Component
public class DataGenerator {
    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShipmentRepository shipmentRepository;

    @Autowired
    LocationTypeRepository locationTypeRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    OutboundShipmentRepository outboundShipmentRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    ShipmentUnitRepository shipmentUnitRepository;

    public SupplierEntity generateSupplier() {
        SupplierEntity supplierEntity = SupplierEntity.builder()
            .name(generateString())
            .code(generateString())
            .build();
        return supplierRepository.save(supplierEntity);
    }

    public ProductEntity generateProduct() {
        ProductEntity productEntity = ProductEntity.builder()
            .name(generateString())
            .code(generateString())
            .lengthCm(generateLong())
            .widthCm(generateLong())
            .heightCm(generateLong())
            .build();
        return productRepository.save(productEntity);
    }

    public ShipmentEntity generateShipment() {
        SupplierEntity supplierEntity = generateSupplier();
        ProductEntity productEntity = generateProduct();
        ShipmentEntity shipmentEntity = ShipmentEntity.builder()
            .supplier(supplierEntity)
            .externalShipmentId(generateString())
            .product(productEntity)
            .status(ShipmentStatus.IN_PROCESS.name())
            .shipmentUnitCount(generateLong())
            .build();
        return shipmentRepository.save(shipmentEntity);
    }

    public LocationTypeEntity generateLocationType() {
        LocationTypeEntity locationTypeEntity = LocationTypeEntity.builder()
            .code(generateString())
            .unlimited(true)
            .build();
        return locationTypeRepository.save(locationTypeEntity);
    }

    public ZoneEntity generateZone(ZoneType zoneType) {
        ZoneEntity zoneEntity = ZoneEntity.builder()
            .code(generateString())
            .type(zoneType.name())
            .build();
        return zoneRepository.save(zoneEntity);
    }

    public LocationEntity generateLocation(ZoneType zoneType) {
        LocationTypeEntity locationTypeEntity = generateLocationType();
        ZoneEntity zoneEntity = generateZone(zoneType);
        LocationEntity locationEntity = LocationEntity.builder()
            .code(generateString())
            .locationType(locationTypeEntity)
            .zone(zoneEntity)
            .build();
        return locationRepository.save(locationEntity);
    }

    public ReceiverEntity generateReceiver() {
        ReceiverEntity receiverEntity = ReceiverEntity.builder()
            .code(generateString())
            .name(generateString())
            .build();
        return receiverRepository.save(receiverEntity);
    }

    public StockEntity generateStock(ProductEntity productEntity, Long amount) {
        LocationEntity locationEntity = generateLocation(ZoneType.STORAGE);
        StockEntity stockEntity = StockEntity.builder()
            .product(productEntity)
            .amount(amount)
            .location(locationEntity)
            .build();
        return stockRepository.save(stockEntity);
    }

    public OutboundShipmentEntity generateOutboundShipment() {
        ReceiverEntity receiverEntity = generateReceiver();
        ProductEntity productEntity = generateProduct();
        OutboundShipmentEntity outboundShipmentEntity = OutboundShipmentEntity.builder()
            .receiver(receiverEntity)
            .externalShipmentId(generateString())
            .status(ShipmentStatus.IN_PROCESS.name())
            .build();
        return outboundShipmentRepository.save(outboundShipmentEntity);
    }

    public WithdrawalEntity generateWithdrawal(OutboundShipmentEntity outboundShipmentEntity) {
        ProductEntity productEntity = generateProduct();
        WithdrawalEntity withdrawalEntity = WithdrawalEntity.builder()
            .outboundShipment(outboundShipmentEntity)
            .product(productEntity)
            .amount(generateLong())
            .status(WithdrawalStatus.CREATED.name())
            .build();
        withdrawalRepository.save(withdrawalEntity);
        return withdrawalEntity;
    }

    public ShipmentUnitEntity generateShipmentUnit() {
        ShipmentEntity shipmentEntity = generateShipment();
        LocationEntity locationEntity = generateLocation(ZoneType.INBOUND);
        ShipmentUnitEntity shipmentUnitEntity = ShipmentUnitEntity.builder()
            .shipment(shipmentEntity)
            .amount(generateLong())
            .widthCm(generateLong())
            .lengthCm(generateLong())
            .heightCm(generateLong())
            .location(locationEntity)
            .build();
        return shipmentUnitRepository.save(shipmentUnitEntity);
    }
}
