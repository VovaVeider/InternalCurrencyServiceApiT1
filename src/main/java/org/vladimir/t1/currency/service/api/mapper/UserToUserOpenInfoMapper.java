package org.vladimir.t1.currency.service.api.mapper;

import org.vladimir.t1.currency.service.api.dto.UserOpenInfo;
import org.vladimir.t1.currency.service.api.entity.User;

public class UserToUserOpenInfoMapper {
    public static UserOpenInfo mapUserToUserOpenInfo(final User user) {
        return new UserOpenInfo(
                user.getUsername(),
                user.getName(),
                user.getLastname(),
                user.getSurname(),
                user.getAccount().getAccountNumber()
        );
    }
}
