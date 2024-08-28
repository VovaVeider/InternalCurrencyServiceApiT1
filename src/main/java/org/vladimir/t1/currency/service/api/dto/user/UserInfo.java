package org.vladimir.t1.currency.service.api.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.vladimir.t1.currency.service.api.entity.UserRole;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record UserInfo(Long userId,
                       String username,
                       UserRole role,
                       String accountNumber,
                       Long accountBalance,
                       String name,
                       String lastname,
                       String surname,
                       String email
                       ) {

}
