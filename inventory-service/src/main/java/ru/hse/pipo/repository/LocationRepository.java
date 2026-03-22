package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}
