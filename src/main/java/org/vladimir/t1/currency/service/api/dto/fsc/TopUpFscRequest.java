package org.vladimir.t1.currency.service.api.dto.fsc;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record TopUpFscRequest(Long id, Long amount) {
}
