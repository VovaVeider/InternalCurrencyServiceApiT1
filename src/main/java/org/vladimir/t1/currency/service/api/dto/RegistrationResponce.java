package org.vladimir.t1.currency.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.vladimir.t1.currency.service.api.entity.UserRole;

@Value
@AllArgsConstructor
@Builder
public class RegistrationResponce {

    Long id;

    String username;

    UserRole role;

    String name;

    String lastname;

    String surname;

    String email;
}
