package ru.hse.pipo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hse.pipo.entity.LocationEntity;
import ru.hse.pipo.entity.LocationTypeEntity;
import ru.hse.pipo.entity.ProductEntity;
import ru.hse.pipo.entity.ShipmentEntity;
import ru.hse.pipo.entity.SupplierEntity;
import ru.hse.pipo.entity.ZoneEntity;
import ru.hse.pipo.model.ShipmentStatus;
import ru.hse.pipo.model.ZoneType;
import ru.hse.pipo.repository.LocationRepository;
import ru.hse.pipo.repository.LocationTypeRepository;
import ru.hse.pipo.repository.ProductRepository;
import ru.hse.pipo.repository.ShipmentRepository;
import ru.hse.pipo.repository.SupplierRepository;
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
}
