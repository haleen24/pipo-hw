package ru.hse.pipo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateOutboundShipmentRequest;
import ru.hse.inventory.model.OutboundShipmentResponse;
import ru.hse.pipo.entity.OutboundShipmentEntity;
import ru.hse.pipo.model.OutboundShipment;
import ru.hse.pipo.model.OutboundShipmentWithWithdrawals;
import ru.hse.pipo.model.Withdrawal;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {WithdrawalMapper.class})
public interface OutboundShipmentMapper {
    @Mapping(target = "receiverCode", source = "outboundShipment.receiver.code")
    @Mapping(target = "withdrawals", source = "withdrawals")
    OutboundShipmentResponse toOutboundShipmentResponse(OutboundShipment outboundShipment, List<Withdrawal> withdrawals);

    @Mapping(target = "outboundShipment.receiver.code", source = "receiverCode")
    @Mapping(target = "outboundShipment.externalShipmentId", source = "externalShipmentId")
    @Mapping(target = "withdrawals", source = "products")
    @Mapping(target = "outboundShipment.id", ignore = true)
    @Mapping(target = "outboundShipment.createdAt", ignore = true)
    @Mapping(target = "outboundShipment.updatedAt", ignore = true)
    @Mapping(target = "outboundShipment.status", ignore = true)
    OutboundShipmentWithWithdrawals toOutboundShipment(CreateOutboundShipmentRequest createOutboundShipmentRequest);

    OutboundShipmentEntity toOutboundShipmentEntity(OutboundShipment outboundShipment);

    OutboundShipment toOutboundShipment(OutboundShipmentEntity outboundShipmentEntity);
}
