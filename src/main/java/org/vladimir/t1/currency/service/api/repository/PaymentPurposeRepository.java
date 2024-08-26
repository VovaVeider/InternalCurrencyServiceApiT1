package org.vladimir.t1.currency.service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vladimir.t1.currency.service.api.entity.PaymentPurpose;

public interface PaymentPurposeRepository extends JpaRepository<PaymentPurpose, Long> {
}
