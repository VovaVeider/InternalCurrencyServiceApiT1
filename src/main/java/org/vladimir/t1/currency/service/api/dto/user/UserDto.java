package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;
import org.vladimir.t1.currency.service.api.entity.User;
import org.vladimir.t1.currency.service.api.entity.UserRole;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDto implements Serializable {
    Long id;
    String username;
    String name;
    String lastname;
    String surname;
    String email;
    UserRole role;
}