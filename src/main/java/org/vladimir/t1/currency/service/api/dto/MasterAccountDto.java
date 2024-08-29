package org.vladimir.t1.currency.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MasterAccountDto {
    private String accountNumber;
    private Long amount;
}
