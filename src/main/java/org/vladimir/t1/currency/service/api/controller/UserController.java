package org.vladimir.t1.currency.service.api.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;
import org.vladimir.t1.currency.service.api.auth.LoginAndRegistrationAuthentication;
import org.vladimir.t1.currency.service.api.dto.*;
import org.vladimir.t1.currency.service.api.exception.ApiException;
import org.vladimir.t1.currency.service.api.repository.PaymentPurposeRepository;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final PaymentPurposeRepository paymentPurposeRepository;

    @PostMapping("")
    public void registerUser(@RequestBody final RegistrationRequest registrationRequest) {
        log.info("Registering user {}", registrationRequest);
        var registrationResponce = userService.registerUser(registrationRequest);
        log.info("Registered user {}", registrationResponce);
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new LoginAndRegistrationAuthentication(registrationResponce.getRole().name(),
                        registrationResponce.getUsername(),
                        registrationResponce.getId()
                )));

    }

    @GetMapping("")
    public GetUsersOpenInfoListResponse getUserOpenInfo(@RequestParam(required = false) String username,
                                                        @RequestParam(required = false) String email,
                                                        @RequestParam @Min(1) int page,
                                                        @Max(100) int size) {
        if (username == null && email == null)
            throw new ApiException("InvalidQueryParamsException",
                    "INVALID_PARAMS", "Username or email are required, but not provides");
        if (username != null && email != null)
            throw new ApiException("InvalidQueryParamsException",
                    "INVALID_PARAMS", "Username and email are provided, but only one must be provided");
        if (username != null)
            return new GetUsersOpenInfoListResponse(userService.findUsersByUsername(username, page, size));
        else
            return new GetUsersOpenInfoListResponse(userService.findUsersByEmail(email, page, size));
    }

    @PostMapping("me/transactions")
    public TransactionReportDto makeTransaction(@AuthenticationPrincipal final Long userId, @RequestBody MakeUserTransactionRequest transactionRequest) {
        return userService.makeTransaction(userId,
                transactionRequest.fromAccountNumber(),
                transactionRequest.toAccountNumber(),
                transactionRequest.amount(),
                transactionRequest.paymentPurpose_id(),
                transactionRequest.paymentComment()
        );
    }

    @GetMapping("me/transactions/payments-purposes")
    public PaymentPurposesRequestDto getAllPaymentPurposes() {
        return new PaymentPurposesRequestDto(
                paymentPurposeRepository.findAll()
                        .stream()
                        .map(p -> new PaymentPurposeDto(p.getId(), p.getName()))
                        .toList()
        );
    }

    @GetMapping("/me")
    public UserInfo getUser(@AuthenticationPrincipal final Long userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("me/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        var login = userService.loginUser(loginRequest);
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new LoginAndRegistrationAuthentication(login.getRole(),
                        login.getUsername(),
                        login.getId()
                )));
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("me/hello")
    public ResponseEntity<Map<String, String>> getString() {
        return ResponseEntity.ok(Map.of("hello", "hello"));
    }


}
