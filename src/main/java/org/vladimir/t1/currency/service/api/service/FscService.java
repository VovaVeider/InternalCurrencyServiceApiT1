package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.fsc.CreateFscRequest;
import org.vladimir.t1.currency.service.api.dto.fsc.CreateFscResponse;
import org.vladimir.t1.currency.service.api.dto.fsc.FSCShortInfo;
import org.vladimir.t1.currency.service.api.dto.fsc.FscInfoDto;
import org.vladimir.t1.currency.service.api.dto.user.TransactionReportDto;
import org.vladimir.t1.currency.service.api.entity.*;
import org.vladimir.t1.currency.service.api.exception.fsc.FscException;
import org.vladimir.t1.currency.service.api.exception.fsc.FscExceptionType;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionException;
import org.vladimir.t1.currency.service.api.exception.transaction.TransactionExceptionType;
import org.vladimir.t1.currency.service.api.mapper.FscMapper;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;
import org.vladimir.t1.currency.service.api.repository.FscRepository;
import org.vladimir.t1.currency.service.api.repository.PaymentPurposeRepository;
import org.vladimir.t1.currency.service.api.repository.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FscService {
    private final FscRepository fscRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final PaymentPurposeRepository purposeRepository;
    private final PaymentPurposeRepository paymentPurposeRepository;

    @Transactional
    public CreateFscResponse createFsc(CreateFscRequest request) {
        long initBalance = request.initBalance() == null ? 0 : request.initBalance();
        if (initBalance < 0)
            throw new FscException(FscExceptionType.NEGATIVE_INIT_BALANCE, "Fsc init balance must be non-negative");
        if (initBalance > 0 && request.fscType().equals(FscType.STORE))
            throw new FscException(FscExceptionType.STORE_FSC_HAVE_NO_INIT_BALANCE, "Store fsc always have zero init balance");
        if (request.fscType().equals(FscType.MASTER))
            throw new FscException(FscExceptionType.MASTER_FSC_CREATE_NOT_PERMITTED, "Master fsc create not permitted, system do it , not admin");

        if (fscRepository.existsByName(request.name()))
            throw new FscException(FscExceptionType.NAME_ALREADY_EXISTS, "Name already exists");

        User owner = null;
        if (request.ownerId() != null) {
            //TODO: СДЕЛАТЬ ПОИСК ПО ИД И РОЛИ СРАЗУ
            owner = userRepository.findById(request.ownerId()).orElseThrow(
                    () -> new FscException(FscExceptionType.FSC_OWNER_NOT_FOUND, "User not found by given id"));
            if (owner.getRole() == UserRole.ROLE_USER)
                throw new FscException(FscExceptionType.USER_HAS_NO_FSC_OWNER_RIGHT, "User has no right to own fsc");
        }

        var fscAccount = Account.builder()
                .accountType(AccountUtils.getAccountType(request.fscType()))
                .disabled(false)
                .balance(0L)
                .build();
        accountRepository.save(fscAccount);

        if (initBalance != 0) {
            var masterAccountOpt = accountRepository.findMasterAccount();
            log.info("Master account is presented {}", masterAccountOpt.isPresent());
            var masterAccount = masterAccountOpt.orElseThrow(() ->
            {
                log.error("Master account not found");
                return new FscException(FscExceptionType.FATAL_MASTER_ACCOUNT_NOT_FOUND, "Master account not found");
            });
            if (masterAccount.getBalance() < initBalance)
                throw new FscException(FscExceptionType.MASTER_FSC_NO_ENOUGH_FUNDS, "Master account balance lower than given init balance");

            var paymentPurpose = purposeRepository.findById((long) SystemPaymentPurposes.SET_FSC_INIT_BALANCE.getId())
                    .orElseThrow(() -> new IllegalStateException("Not found system payment purpose: SET_INIT_BALANCE"));

            transactionService.makeTransaction(masterAccount.getAccountNumber(), fscAccount.getAccountNumber(), initBalance,
                    paymentPurpose, "Set init balance for created FSC by system");
        }

        var fsc = FSC.builder()
                .name(request.name())
                .account(fscAccount)
                .owner(owner)
                .fscType(request.fscType())
                .createdAt(Instant.now())
                .disabled(false)
                .build();
        fscRepository.save(fsc);

        return new CreateFscResponse(fsc.getId(), fscAccount.getAccountNumber());
    }

    @Transactional
    public List<FSCShortInfo> getFscShortInfoList(FscType fscType, int page, int size) {
        //TODO: ОПТИМИЗАЦИЯ СРОЧНО
        Pageable pageable = PageRequest.of(page, size);
        Page<FSC> fscPage = fscRepository.findByFscType(fscType, pageable);
        return fscPage.stream().map(FscMapper::toFSCShortInfo).toList();
    }

    @Transactional
    public List<FscInfoDto> getUserFscList(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<FSC> fscPage = fscRepository.findActiveTeamFsc(name, pageable);
        return fscPage.stream().map(FscMapper::toFscInfoDto).peek(fscInfoDto -> {
            fscInfoDto.setBalance(null);
            fscInfoDto.setOwnerRole(null);
        }).toList();
    }

    @Transactional
    public TransactionReportDto topUpFscAccount(Long id, Long amount) {

        var toAccount = fscRepository.findById(id).
                orElseThrow(() -> new FscException(FscExceptionType.FSC_NOT_FOUND, ""))
                .getAccount().getAccountNumber();

        return transactionService.makeTransaction(
                accountRepository.findMasterAccount()
                        .orElseThrow(() -> new FscException(FscExceptionType.FATAL_MASTER_ACCOUNT_NOT_FOUND, "")).getAccountNumber(),
                toAccount,
                amount,
                purposeRepository.findById(SystemPaymentPurposes.TOP_FSC_BALANCE_FROM_MASTER.getId())
                        .orElseThrow(() -> new TransactionException(TransactionExceptionType.PAYMENT_PURPOSE_NOT_SUPPORTED, "")),
                ""
        );
    }

    @Transactional
    public TransactionReportDto makeTransaction(Long fscId,
                                                String accountNumber,
                                                Long amount,
                                                Long paymentPurposeId,
                                                String paymentComment) {
        //TODO: ДОБАВИТЬ проверку на админа и проверку ЦФО
        var fromFSC = fscRepository.findById(fscId).orElseThrow(
                () -> new FscException(FscExceptionType.FSC_NOT_FOUND, ""));
        return transactionService.makeTransaction(fromFSC.getAccount().getAccountNumber(),
                accountNumber, amount, paymentPurposeRepository.findById(paymentPurposeId)
                        .orElseThrow(
                                () -> new TransactionException(TransactionExceptionType.PAYMENT_PURPOSE_NOT_SUPPORTED,
                                        "This payment purpose not exists")),
                paymentComment);

    }


    @Transactional
    public FscInfoDto getFscMainInfo(Long id) {
        return FscMapper.toFscInfoDto(fscRepository.findById(id).orElseThrow(() -> new FscException(FscExceptionType.FSC_NOT_FOUND, "")));
    }
}
