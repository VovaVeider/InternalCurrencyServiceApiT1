package org.vladimir.t1.currency.service.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    String username;

    String name;

    String lastname;

    String surname;

    String email;

    String password;
}
