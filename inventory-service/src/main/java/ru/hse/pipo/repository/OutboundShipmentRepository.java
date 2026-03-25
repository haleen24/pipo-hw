package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.OutboundShipmentEntity;

@Repository
public interface OutboundShipmentRepository extends JpaRepository<OutboundShipmentEntity, Long> {
}
