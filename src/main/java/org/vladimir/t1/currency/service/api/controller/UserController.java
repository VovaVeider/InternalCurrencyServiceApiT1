package org.vladimir.t1.currency.service.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;
import org.vladimir.t1.currency.service.api.auth.LoginAndRegistrationAuthentication;
import org.vladimir.t1.currency.service.api.dto.OneFieldDto;
import org.vladimir.t1.currency.service.api.dto.transaction.PaymentPurposeDto;
import org.vladimir.t1.currency.service.api.dto.user.*;
import org.vladimir.t1.currency.service.api.entity.UserRole;
import org.vladimir.t1.currency.service.api.exception.ApiException;
import org.vladimir.t1.currency.service.api.service.PaymentPurposeService;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")

public class UserController {

    private final UserService userService;
    private final PaymentPurposeService paymentPurposeService;

    @PostMapping("")
    public void registerUser(@RequestBody final RegistrationRequest registrationRequest) {
        log.info("Registering user {}", registrationRequest);
        var registrationResponce = userService.registerUser(registrationRequest);
        log.info("Registered user {}", registrationResponce);
        SecurityContextHolder.setContext(new SecurityContextImpl(new LoginAndRegistrationAuthentication(registrationResponce.getRole().name(), registrationResponce.getUsername(), registrationResponce.getId())));

    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("")
    public GetUsersOpenInfoListResponse getUserOpenInfo(@RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam @Min(1) int page, @Max(100) int size) {
        if (username == null && email == null)
            throw new ApiException("InvalidQueryParamsException", "INVALID_PARAMS", "Username or email are required, but not provides");
        if (username != null && email != null)
            throw new ApiException("InvalidQueryParamsException", "INVALID_PARAMS", "Username and email are provided, but only one must be provided");
        if (username != null)
            return new GetUsersOpenInfoListResponse(userService.findUsersByUsername(username, page, size, Arrays.asList(UserRole.values())));
        else
            return new GetUsersOpenInfoListResponse(userService.findUsersByEmail(email, page, size, Arrays.asList(UserRole.values())));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("fsc-owners")
    public AdminsAndFscOwnersResponce getFscOwners(@RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam @Min(1) int page, @Max(100) int size) {
        if (username == null && email == null)
            throw new ApiException("InvalidQueryParamsException", "INVALID_PARAMS", "Username or email are required, but not provides");
        if (username != null && email != null)
            throw new ApiException("InvalidQueryParamsException", "INVALID_PARAMS", "Username and email are provided, but only one must be provided");
        if (username != null)
            return new AdminsAndFscOwnersResponce(userService.findUsersProfilesByUsername(username, page, size, List.of(UserRole.ROLE_FSC_OWNER, UserRole.ROLE_ADMIN)));

        else
            return new AdminsAndFscOwnersResponce(userService.findUsersProfilesByEmail(email, page, size, List.of(UserRole.ROLE_FSC_OWNER, UserRole.ROLE_ADMIN)));
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping("me/transactions")
    public TransactionReportDto makeTransaction(@AuthenticationPrincipal final Long userId, @RequestBody MakeUserTransactionRequest transactionRequest) {
        return userService.makeTransaction(userId, transactionRequest.fromAccountNumber(), transactionRequest.toAccountNumber(), transactionRequest.amount(), transactionRequest.paymentPurpose_id(), transactionRequest.paymentComment());
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/payments-purposes")
    public OneFieldDto<List<PaymentPurposeDto>> getAllPaymentPurposes() {
        return new OneFieldDto<>(paymentPurposeService.getUserToUserPaymentPurposes());
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public UserInfo getUser(@AuthenticationPrincipal final Long userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("me/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        var login = userService.loginUser(loginRequest);
        SecurityContextHolder.setContext(new SecurityContextImpl(new LoginAndRegistrationAuthentication(login.getRole(), login.getUsername(), login.getId())));
    }


}
