package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record MakeUserTransactionRequest(String fromAccountNumber, String toAccountNumber, Long amount, Long paymentPurpose_id,
                                         String paymentComment) {
}
