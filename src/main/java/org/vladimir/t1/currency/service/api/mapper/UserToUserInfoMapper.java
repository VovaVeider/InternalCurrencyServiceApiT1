package org.vladimir.t1.currency.service.api.mapper;

import org.vladimir.t1.currency.service.api.dto.user.UserInfo;
import org.vladimir.t1.currency.service.api.entity.User;

public class UserToUserInfoMapper {
    public static UserInfo  fromUserToUserInfo(User user) {
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getAccount().getAccountNumber(),
                user.getAccount().getBalance(),
                user.getName(),
                user.getLastname(),
                user.getSurname(),
                user.getEmail()
        );
    }
}
