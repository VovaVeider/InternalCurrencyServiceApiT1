package org.vladimir.t1.currency.service.api.dto.fsc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FscMakeTransaction {
    String toAccountNumber;
    Long amount;
    Long purposeId;
    String paymentComment;
}
