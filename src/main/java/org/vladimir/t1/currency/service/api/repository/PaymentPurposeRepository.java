package org.vladimir.t1.currency.service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vladimir.t1.currency.service.api.entity.PaymentPurpose;
import org.vladimir.t1.currency.service.api.entity.PaymentPurposeType;

import java.util.List;
import java.util.Optional;

public interface PaymentPurposeRepository extends JpaRepository<PaymentPurpose, Long> {

    Optional<PaymentPurpose> findByIdAndType(Long id, PaymentPurposeType type);

    List<PaymentPurpose> findByType(PaymentPurposeType type);

    List<PaymentPurpose> findByTypeIn(List<PaymentPurposeType> type);
}
