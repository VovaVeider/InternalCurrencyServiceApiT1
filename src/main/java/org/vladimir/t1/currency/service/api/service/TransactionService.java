package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.TransactionReportDto;
import org.vladimir.t1.currency.service.api.entity.*;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionException;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionExceptionType;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.TransactionRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static org.vladimir.t1.currency.service.api.entity.AccountUtils.isFscAccount;
import static org.vladimir.t1.currency.service.api.entity.AccountUtils.isUserAccount;
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionReportDto makeTransaction(String fromAccountNumber,
                                                String toAccountNumber,
                                                Long amount,
                                                PaymentPurpose paymentPurpose,
                                                String paymentComment) {

        validateTransactionType(fromAccountNumber, toAccountNumber);

        Optional<Account> fromAccountOptional = accountRepository.findByAccountNumber(fromAccountNumber);
        if (fromAccountOptional.isEmpty())
            throw new TransactionException(TransactionExceptionType.FROM_ACCOUNT_NOT_FOUND, "Sender account not found");
        var fromAccount = fromAccountOptional.get();
        if (fromAccount.getDisabled())
            throw new TransactionException(TransactionExceptionType.FROM_ACCOUNT_BLOCKED, "Sender account blocked");
        if (fromAccount.getBalance() < amount)
            throw new TransactionException(TransactionExceptionType.NOT_ENOUGH_FUNDS, "Sender account not enough funds");

        Optional<Account> toAccountOptional = accountRepository.findByAccountNumber(toAccountNumber);
        if (toAccountOptional.isEmpty())
            throw new TransactionException(TransactionExceptionType.TO_ACCOUNT_NOT_FOUND, "Receiver account not found");
        var toAccount = toAccountOptional.get();
        if (toAccount.getDisabled())
            throw new TransactionException(TransactionExceptionType.TO_ACCOUNT_BLOCKED, "Receiver account blocked");

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .amount(amount)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .time(Instant.now())
                .paymentPurpose(paymentPurpose)
                .paymentComment(paymentComment)
                .build()
        );
        return new TransactionReportDto(transaction.getId(), fromAccountNumber, toAccountNumber, transaction.getTime().getEpochSecond(), amount);
    }

    private void validateTransactionType(String fromAccountNumber, String toAccountNumber) {

        if (Objects.equals(fromAccountNumber, toAccountNumber))
            throw new TransactionException(TransactionExceptionType.SENDER_AND_RECEIVER_EQUALS, "Sender and reciever is the same");

        var fromType = AccountUtils.getAccountType(fromAccountNumber);
        var toType = AccountUtils.getAccountType(toAccountNumber);
        var validExType = TransactionExceptionType.TRANSACTION_VALIDATION_FAILED;

        if (isUserAccount(fromAccountNumber) && isFscAccount(toAccountNumber))
            throw new TransactionException(validExType, "User cant send money to  to FSC account ");
        if (AccountType.STORE_CFO_ACCOUNT == fromType)
            throw new TransactionException(validExType, "Store  cant send money, it cam only receive");
        if (isFscAccount(fromAccountNumber) && AccountType.STORE_CFO_ACCOUNT == toType)
            throw new TransactionException(validExType, "Fsc  cant send money to store fsc, it can only users");
        if (AccountType.MASTER_FSC_ACCOUNT == fromType && isUserAccount(toAccountNumber))
            throw new TransactionException(validExType, "Master can sent money only to team fsc");

    }
}
