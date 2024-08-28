package org.vladimir.t1.currency.service.api.dto.fsc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.vladimir.t1.currency.service.api.entity.FscType;

public record CreateFscRequest(@NotNull @NotBlank String name, @NotNull FscType fscType, Long initBalance, Long ownerId) {
}
