package org.vladimir.t1.currency.service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vladimir.t1.currency.service.api.entity.Account;
import org.vladimir.t1.currency.service.api.entity.Transaction;

import java.time.Instant;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("""
            select coalesce(sum(t.amount),0)
            from Transaction t
            where t.fromAccount=:account and
            t.time >= :fromTime and
            t.time <= :toTime
            """)
    public long sumTransactionsFromAccount(Account account, Instant fromTime, Instant toTime);

    @Query("""
            select coalesce(sum(t.amount),0)
            from Transaction t
            where t.fromAccount.accountNumber like '002%'and
            t.time >= :fromTime and 
            t.time <= :toTime
            """)
    public long sumTransactionsFromMasterAccount(Instant fromTime, Instant toTime);


    @Query("""
            select coalesce(sum(t.amount),0)
            from Transaction t
            where t.toAccount.accountNumber like '004%' and
            t.time >= :fromTime and
            t.time <= :toTime
            """)
    public long sumTransactionsToStoreAccounts(Instant fromTime, Instant toTime);
}
