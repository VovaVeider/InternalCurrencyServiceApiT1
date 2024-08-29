package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vladimir.t1.currency.service.api.dto.MasterAccountDto;
import org.vladimir.t1.currency.service.api.exception.fsc.FscException;
import org.vladimir.t1.currency.service.api.exception.fsc.FscExceptionType;
import org.vladimir.t1.currency.service.api.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final AccountRepository accountRepository;

    @Transactional
    public MasterAccountDto getMasterAccount() {
        var masterAccount = accountRepository.findMasterAccount().orElseThrow(
                ()->new FscException(FscExceptionType.FATAL_MASTER_ACCOUNT_NOT_FOUND,"Master account not found"));
        return new MasterAccountDto(masterAccount.getAccountNumber(), masterAccount.getBalance());
    }

}
