package ru.hse.pipo.repository;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.LocationTypeEntity;

@Repository
public interface LocationTypeRepository extends JpaRepository<LocationTypeEntity, Long> {
    @Nullable
    LocationTypeEntity findByCode(String code);
}
