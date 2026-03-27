package ru.hse.pipo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.pipo.entity.WithdrawalEntity;

public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, Long> {
    List<WithdrawalEntity> findAllByOutboundShipmentId(Long outboundShipmentId);
}
