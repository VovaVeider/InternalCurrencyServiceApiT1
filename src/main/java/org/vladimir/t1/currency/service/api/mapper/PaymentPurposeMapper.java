package org.vladimir.t1.currency.service.api.mapper;

import org.vladimir.t1.currency.service.api.dto.transaction.PaymentPurposeDto;
import org.vladimir.t1.currency.service.api.entity.PaymentPurpose;

public class PaymentPurposeMapper {
    public static PaymentPurposeDto map(final PaymentPurpose paymentPurpose) {
        return new PaymentPurposeDto(
                paymentPurpose.getId(),
                paymentPurpose.getName()
        );
    }
}
