package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.ZoneEntity;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {
}
