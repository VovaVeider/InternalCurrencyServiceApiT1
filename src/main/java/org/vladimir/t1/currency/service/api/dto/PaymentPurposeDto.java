package org.vladimir.t1.currency.service.api.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.vladimir.t1.currency.service.api.entity.PaymentPurpose}
 */
@Value
public class PaymentPurposeDto implements Serializable {
    Long id;
    String name;
}