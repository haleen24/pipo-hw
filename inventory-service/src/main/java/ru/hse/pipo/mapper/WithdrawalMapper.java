package ru.hse.pipo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateOutboundShipmentRequestProductsInner;
import ru.hse.pipo.entity.WithdrawalEntity;
import ru.hse.pipo.model.Withdrawal;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface WithdrawalMapper {
    @Mapping(target = "amount", source = "productCount")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "outboundShipmentEntity", ignore = true)
    @Mapping(target = "status", ignore = true)
    Withdrawal toWithdrawal(CreateOutboundShipmentRequestProductsInner createOutboundShipmentRequestProductsInner);

    List<Withdrawal> toWithdrawalsFromCreateOutboundShipmentRequestProductsInner(
        List<CreateOutboundShipmentRequestProductsInner> createOutboundShipmentRequestProductsInners);

    WithdrawalEntity toWithdrawalEntity(Withdrawal withdrawal);

    List<Withdrawal> toWithdrawals(List<WithdrawalEntity> withdrawalEntities);
}
