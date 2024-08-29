package org.vladimir.t1.currency.service.api.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vladimir.t1.currency.service.api.entity.Account;
import org.vladimir.t1.currency.service.api.entity.AccountType;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String fromAccountNumber);
    Optional<Account> findByAccountNumberStartingWith(String fromAccountNumber);

    @Cacheable("MasterAccount")
    default Optional<Account> findMasterAccount() {
        var acc = findByAccountNumberStartingWith(
                AccountType.getFormattedAccountTypeCode(AccountType.MASTER_FSC_ACCOUNT)
        );
        return acc;
    }
}
