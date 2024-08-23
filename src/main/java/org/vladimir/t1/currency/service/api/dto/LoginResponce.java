package org.vladimir.t1.currency.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class LoginResponce {
    Integer id;
    String role;
    String username;
}
