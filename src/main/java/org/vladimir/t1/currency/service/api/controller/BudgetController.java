package org.vladimir.t1.currency.service.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vladimir.t1.currency.service.api.dto.MasterAccountDto;
import org.vladimir.t1.currency.service.api.dto.fsc.TopUpMasterAccountRequest;
import org.vladimir.t1.currency.service.api.entity.MasterAccountAction;
import org.vladimir.t1.currency.service.api.exception.fsc.FscException;
import org.vladimir.t1.currency.service.api.exception.fsc.FscExceptionType;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.MasterAccountActionRepository;
import org.vladimir.t1.currency.service.api.service.BudgetService;
import org.vladimir.t1.currency.service.api.service.TransactionService;

import java.time.Instant;

@RestController
@RequestMapping("/budget")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class BudgetController {
    private final BudgetService budgetService;
    private final AccountRepository accountRepository;
    private final MasterAccountActionRepository masterAccountActionRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public MasterAccountDto getMasterAccountInfo() {
        return budgetService.getMasterAccount();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void topUpMasterAccount(TopUpMasterAccountRequest dto) {
        if (dto.amount() == null)
            throw new IllegalArgumentException("Amount must not be null");
        if (dto.amount() <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        var master = accountRepository.findMasterAccount().orElseThrow(
                () -> new FscException(FscExceptionType.FATAL_MASTER_ACCOUNT_NOT_FOUND, "")
        );
        master.setBalance(master.getBalance() + dto.amount());
        masterAccountActionRepository.save(MasterAccountAction.builder()
                .amount(dto.amount())
                .time(Instant.now())
                .build());

    }
}
