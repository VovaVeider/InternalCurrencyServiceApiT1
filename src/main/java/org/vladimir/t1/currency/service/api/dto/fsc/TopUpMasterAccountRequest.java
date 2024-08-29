package org.vladimir.t1.currency.service.api.dto.fsc;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TopUpMasterAccountRequest (@NotNull @Positive Long amount){
}
