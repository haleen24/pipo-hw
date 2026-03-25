package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateOutboundShipmentRequest;
import ru.hse.inventory.model.OutboundShipmentResponse;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.model.OutboundShipment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {WithdrawalMapper.class})
public interface OutboundShipmentMapper {
    @Mapping(target = "receiverCode", source = "receiver.code")
    OutboundShipmentResponse toOutboundShipmentResponse(OutboundShipment outboundShipment);

    @Mapping(target = "receiver.code", source = "receiverCode")
    @Mapping(target = "withdrawals", source = "products")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    OutboundShipment toOutboundShipment(CreateOutboundShipmentRequest createOutboundShipmentRequest);

    OutboundShipmentEntity toOutboundShipmentEntity(OutboundShipment outboundShipment);

    @Mapping(target = "withdrawals", ignore = true)
    OutboundShipment toOutboundShipment(OutboundShipmentEntity outboundShipmentEntity);
}
