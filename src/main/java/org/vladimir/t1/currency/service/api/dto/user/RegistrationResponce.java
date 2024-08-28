package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.vladimir.t1.currency.service.api.entity.UserRole;

@Value
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegistrationResponce {

    Long id;

    String username;

    UserRole role;

    String name;

    String lastname;

    String surname;

    String email;
}
