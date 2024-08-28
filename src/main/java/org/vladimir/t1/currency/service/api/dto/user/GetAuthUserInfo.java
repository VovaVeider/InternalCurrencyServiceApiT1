package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record GetAuthUserInfo(String username, String role, Boolean disabled) {

}
