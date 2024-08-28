package org.vladimir.t1.currency.service.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vladimir.t1.currency.service.api.entity.FSC;
import org.vladimir.t1.currency.service.api.entity.FscType;

import java.util.Optional;

public interface FscRepository extends JpaRepository<FSC, Long> {

    boolean existsByName(String name);

    Page<FSC> findByFscType(FscType fscType, Pageable pageable);

    @Query("select f from FSC f join fetch Account a on a.id=f.account.id where " +
            "f.disabled=false and " +
            "f.fscType=org.vladimir.t1.currency.service.api.entity.FscType.TEAM and " +
            "lower(f.name) like CONCAT(lower(:name),'%')")
    Slice<FSC> findActiveTeamFsc(String name, Pageable pageable);

    @Query("select f from FSC  f where f.account.accountNumber = :accountNumber")
    Optional<FSC> findByAccountNumber(String accountNumber);



}
