package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.user.*;
import org.vladimir.t1.currency.service.api.entity.*;
import org.vladimir.t1.currency.service.api.exception.login.LoginException;
import org.vladimir.t1.currency.service.api.exception.login.LoginExceptionType;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationException;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationExceptionType;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionException;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionExceptionType;
import org.vladimir.t1.currency.service.api.mapper.UserToUserInfoMapper;
import org.vladimir.t1.currency.service.api.mapper.UserToUserOpenInfoMapper;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.PaymentPurposeRepository;
import org.vladimir.t1.currency.service.api.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final PaymentPurposeRepository paymentPurposeRepository;

    @Transactional
    public RegistrationResponce registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername()))
            throw new RegistrationException(RegistrationExceptionType.USERNAME_ALREADY_EXISTS, "This username already exists");
        if (userRepository.existsByEmail(registrationRequest.getEmail()))
            throw new RegistrationException(RegistrationExceptionType.EMAIL_ALREADY_EXISTS, "This email already exists");

        var rowPassword = registrationRequest.getPassword();
        var password = passwordEncoder.encode(rowPassword);
        Account account = Account.builder()
                .balance(0L)
                .accountType(AccountType.USER_ACCOUNT)
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
        if (optionalUser.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword()))
            throw new LoginException(LoginExceptionType.USER_NOT_FOUND, "User with same username and password wasn't found");

        var user = optionalUser.get();
        if (user.getDisabled())
            throw new LoginException(LoginExceptionType.USER_IS_BLOCKED, "User is blocked");

        return LoginResponce.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();

    }

    public UserInfo getUserInfo(Long userId) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new LoginException(LoginExceptionType.USER_NOT_FOUND, "User with same username wasn't found");
        return UserToUserInfoMapper.fromUserToUserInfo(optionalUser.get());
    }

    @Transactional
    public List<UserOpenInfo> findUsersByUsername(String username, int page, int pageSize, List<UserRole> userRoles) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> usersPage = userRepository.findAllByUsernameStartingWithAndRoleIn(pageable, username, userRoles);
        var result = usersPage.stream().map(UserToUserOpenInfoMapper::mapUserToUserOpenInfo).toList();
        return result;
    }

    public List<UserOpenInfo> findUsersByEmail(String username, int page, int pageSize, List<UserRole> userRoles) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> usersPage = userRepository.findAllByEmailStartingWithAndRoleIn(pageable, username, userRoles);
        var result = usersPage.stream().map(UserToUserOpenInfoMapper::mapUserToUserOpenInfo).toList();
        return result;
    }

    @Transactional
    public List<UserDto> findUsersProfilesByUsername(String username, int page, int pageSize, List<UserRole> userRoles) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> usersPage = userRepository.findAllByUsernameStartingWithAndRoleIn(pageable, username, userRoles);
        var result = usersPage.stream().map(user -> new UserDto(user.getId(),
                user.getUsername(),
                user.getName(),
                user.getLastname(),
                user.getSurname(),
                user.getEmail(),
                user.getRole())
        ).toList();
        return result;
    }

    public List<UserDto> findUsersProfilesByEmail(String username, int page, int pageSize, List<UserRole> userRoles) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<User> usersPage = userRepository.findAllByEmailStartingWithAndRoleIn(pageable, username, userRoles);
        var result = usersPage.stream().map(user -> new UserDto(user.getId(),
                user.getUsername(),
                user.getName(),
                user.getLastname(),
                user.getSurname(),
                user.getEmail(),
                user.getRole())
        ).toList();
        return result;
    }


    //Используется при проверке измений при выдачи рефреш токена
    @Transactional
    public GetAuthUserInfo getAuthUserInfo(Long userId) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            return new GetAuthUserInfo(user.getUsername(), user.getRole().name(), user.getDisabled());
        } else {
            return null;
        }
    }


    @Transactional()
    public TransactionReportDto makeTransaction(Long userId, String fromAccountNumber, String toAccountNumber,
                                                Long amount, Long paymentPurposeId, String paymentComment) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new LoginException(LoginExceptionType.USER_NOT_FOUND, "User with same id wasn't found");
        var user = optionalUser.get();

        if (!Objects.equals(user.getAccount().getAccountNumber(), fromAccountNumber))
            throw new TransactionException(TransactionExceptionType.TRANSACTION_VALIDATION_FAILED, "User is not owner of  from account ");

        var paymentPurpose = paymentPurposeRepository.findByIdAndType(paymentPurposeId, PaymentPurposeType.USER_TO_USER).orElseThrow(() ->
                new TransactionException(
                        TransactionExceptionType.PAYMENT_PURPOSE_NOT_SUPPORTED, "Incorrect payment purpose"
                )
        );
        var report = transactionService.makeTransaction(fromAccountNumber, toAccountNumber, amount, paymentPurpose, paymentComment);
        return report;
    }
}
