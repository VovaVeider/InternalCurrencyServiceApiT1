package org.vladimir.t1.currency.service.api.dto.transaction;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.vladimir.t1.currency.service.api.entity.PaymentPurpose}
 */
@Value
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentPurposeDto implements Serializable {
    Long id;
    String name;
}