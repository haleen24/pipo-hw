package ru.hse.pipo.repository;

import java.util.List;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    List<StockEntity> findAllByProductCode(String productCode);

    List<StockEntity> findAllByLocationCode(String locationCode);

    @Nullable
    StockEntity findByLocationCodeAndProductCode(String locationCode, String productCode);
}
