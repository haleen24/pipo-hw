package ru.hse.pipo.service;

import ru.hse.pipo.model.OutboundShipment;

public interface OutboundService {
    OutboundShipment create(OutboundShipment outboundShipment);

    OutboundShipment get(Long id);

    OutboundShipment fail(Long id);
}
