package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.ShipmentUnitEntity;

@Repository
public interface ShipmentUnitRepository extends JpaRepository<ShipmentUnitEntity, Long> {
}
