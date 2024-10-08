package org.vladimir.t1.currency.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
