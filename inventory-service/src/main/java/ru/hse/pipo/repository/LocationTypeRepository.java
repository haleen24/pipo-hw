package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.LocationTypeEntity;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationTypeEntity, Long> {
}
