package ru.hse.pipo.repository;

import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.pipo.entity.ReceiverEntity;

@Repository
public interface ReceiverRepository extends JpaRepository<ReceiverEntity, Long> {
    @Nullable
    ReceiverEntity findByCode(String code);
}
