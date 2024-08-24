package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.*;
import org.vladimir.t1.currency.service.api.entity.Account;
import org.vladimir.t1.currency.service.api.entity.User;
import org.vladimir.t1.currency.service.api.entity.UserRole;
import org.vladimir.t1.currency.service.api.exception.login.LoginException;
import org.vladimir.t1.currency.service.api.exception.login.LoginExceptionType;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationException;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationExceptionType;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.UserRepository;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public RegistrationResponce registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername()))
            throw new RegistrationException(RegistrationExceptionType.USERNAME_ALREADY_EXISTS, "This username already exists");
        if (userRepository.existsByEmail(registrationRequest.getEmail()))
            throw new RegistrationException(RegistrationExceptionType.EMAIL_ALREADY_EXISTS, "This email already exists");

        var rowPassword = registrationRequest.getPassword();
        var password = passwordEncoder.encode(rowPassword);
        Account account = Account.builder()
                .balance(0)
                .userAccount(true)
                .disabled(false)
                .build();
        accountRepository.save(account);
        var user = User.builder()
                .username(registrationRequest.getUsername())
                .password(password)
                .role(UserRole.ROLE_USER)
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .lastname(registrationRequest.getLastname())
                .surname(registrationRequest.getSurname())
                .account(account)
                .createdAt(Instant.now())
                .disabled(false)
                .build();

        userRepository.save(user);

        return RegistrationResponce.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .lastname(registrationRequest.getLastname())
                .surname(registrationRequest.getSurname())
                .id(user.getId())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public LoginResponce loginUser(LoginRequest loginRequest) {
        var optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return LoginResponce.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name())
                        .build();
            }
        }
        throw new LoginException(LoginExceptionType.USER_NOT_FOUND, "User with same username and password wasnt found");

    }

    @Transactional
    public UserInfo getUserInfo (Integer userId){
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            return new UserInfo(user.getUsername(), user.getRole().name(),user.getDisabled());
        } else{
            return null;
        }
    }
}
