package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.ShipmentEntity;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, Long> {
}
