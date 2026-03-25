package ru.hse.pipo.service;


import ru.hse.pipo.model.Receiver;

public interface ReceiverService {
    Receiver create(Receiver receiver);

    Receiver getByCode(String code);
}
