package org.vladimir.t1.currency.service.api.dto.user;

import org.vladimir.t1.currency.service.api.entity.UserRole;


public record UserCreateFscResponce(
        Long userId,
        String username,
        UserRole role,
        String name,
        String lastname,
        String surname,
        String email) {
}
