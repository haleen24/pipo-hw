package ru.hse.pipo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.pipo.entity.WithdrawalEntity;

public interface WithdrawalRepository extends JpaRepository<WithdrawalEntity, Long> {
}
