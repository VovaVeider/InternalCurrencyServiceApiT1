package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.MasterAccountDto;
import org.vladimir.t1.currency.service.api.dto.MasterAccountShortAnalitycsReport;
import org.vladimir.t1.currency.service.api.exception.fsc.FscException;
import org.vladimir.t1.currency.service.api.exception.fsc.FscExceptionType;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.MasterAccountActionRepository;
import org.vladimir.t1.currency.service.api.repository.TransactionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final AccountRepository accountRepository;
    private final MasterAccountActionRepository masterAccountActionRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public MasterAccountDto getMasterAccount() {
        var masterAccount = accountRepository.findMasterAccount().orElseThrow(
                () -> new FscException(FscExceptionType.FATAL_MASTER_ACCOUNT_NOT_FOUND, "Master account not found"));
        return new MasterAccountDto(masterAccount.getAccountNumber(), masterAccount.getBalance());
    }

    @Transactional
    public MasterAccountShortAnalitycsReport getShortAnalytics() {
        //TODO:Аналитика по периодам, сейчас с начального дня месяца для фронта
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        Instant toTime = now.atZone(ZoneId.systemDefault()).toInstant();
        Instant fromTime = monthStart.atZone(ZoneId.systemDefault()).toInstant();

        Long amountEntered = masterAccountActionRepository.getTotalAmountDepositedInRange(fromTime, toTime);
        Long amountTransferedToFSC = transactionRepository.sumTransactionsFromAccount(
                accountRepository.findMasterAccount().get(), fromTime, toTime
        );
        Long amountWithdrawnFromFSC = transactionRepository.sumTransactionsToStoreAccounts(fromTime, toTime);

        return new MasterAccountShortAnalitycsReport(amountEntered, amountTransferedToFSC, amountWithdrawnFromFSC);
    }
}

