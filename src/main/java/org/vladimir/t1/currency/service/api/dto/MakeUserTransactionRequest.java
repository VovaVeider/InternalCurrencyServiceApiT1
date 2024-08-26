package org.vladimir.t1.currency.service.api.dto;

public record MakeUserTransactionRequest(String fromAccountNumber, String toAccountNumber, Long amount, Long paymentPurpose_id,
                                         String paymentComment) {
}
