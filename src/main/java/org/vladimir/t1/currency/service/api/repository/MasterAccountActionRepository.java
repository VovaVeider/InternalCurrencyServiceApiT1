package org.vladimir.t1.currency.service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vladimir.t1.currency.service.api.entity.MasterAccountAction;

import java.time.Instant;

public interface MasterAccountActionRepository extends JpaRepository<MasterAccountAction, Long> {
    @Query("select sum(m.amount) from MasterAccountAction m where m.time>=:fromTime and m.time <= :toTime")
    Long getTotalAmountDepositedInRange(Instant fromTime, Instant toTime);

}
