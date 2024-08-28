package org.vladimir.t1.currency.service.api.dto.fsc;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.vladimir.t1.currency.service.api.entity.FscType;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record CreateFscRequest(@NotNull @NotBlank String name, @NotNull FscType fscType, Long initBalance, Long ownerId) {
}
