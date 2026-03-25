package ru.hse.pipo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hse.inventory.model.CreateReceiverRequest;
import ru.hse.inventory.model.ReceiverResponse;
import ru.hse.pipo.entity.ReceiverEntity;
import ru.hse.pipo.model.Receiver;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ReceiverMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Receiver toReceiver(CreateReceiverRequest createReceiverRequest);

    ReceiverResponse toReceiverResponse(Receiver receiver);

    ReceiverEntity toReceiverEntity(Receiver receiver);

    Receiver toReceiver(ReceiverEntity receiver);
}
