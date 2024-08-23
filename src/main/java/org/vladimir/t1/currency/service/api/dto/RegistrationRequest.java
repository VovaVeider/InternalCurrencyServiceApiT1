package org.vladimir.t1.currency.service.api.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
