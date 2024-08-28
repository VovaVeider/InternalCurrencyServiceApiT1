package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AdminsAndFscOwnersResponce(List<UserDto> users) {
}
