package org.vladimir.t1.currency.service.api.dto.user;

public record TransactionReportDto(Long transactionId,
                                   String fromAccountNumber,
                                   String toAccountNumber,
                                   Long timestamp,
                                   Long amount) {
}
