package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_id_seq")
    @SequenceGenerator(name = "accounts_id_seq", sequenceName = "accounts_id_seq", allocationSize = 1)
    private Long id;

    @Builder.Default
    @Column(nullable = false)
    private Boolean disabled = false;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false, length = 12, unique = true,name = "account_number")
    private String accountNumber;//Первые 3 цифры слева номер типа счет,а остальные справа id счета

    @Transient
    AccountType accountType;

    @Column(nullable = false)
    private Long balance;

    @Transient
    public boolean isUserAccount() {
        return accountType.equals(AccountType.USER_ACCOUNT);
    }

    @NotNull
    @Transient
    public boolean isFscAccount() {
        return !isUserAccount();
    }


    @PrePersist
    private void prePersist() { // Устанавливаем номер счета
        if (id == null)
            throw new IllegalStateException("Account must has id before save, but it has null value");

        if (accountType != null)
            accountNumber = AccountUtils.getAccountNumber(accountType,id);
        else
            throw new IllegalStateException("Account must has account type before save, but it has null value");
    }

    @PostLoad
    private void postLoad() {
        accountType = AccountUtils.getAccountType(accountNumber);
    }


}
