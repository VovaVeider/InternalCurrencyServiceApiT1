package org.vladimir.t1.currency.service.api.mapper;

import org.vladimir.t1.currency.service.api.dto.fsc.FSCShortInfo;
import org.vladimir.t1.currency.service.api.dto.fsc.FscInfoDto;
import org.vladimir.t1.currency.service.api.entity.Account;
import org.vladimir.t1.currency.service.api.entity.FSC;
import org.vladimir.t1.currency.service.api.entity.User;
import org.vladimir.t1.currency.service.api.entity.UserRole;

public class FscMapper {
    public static FSCShortInfo toFSCShortInfo(FSC fsc) {
        if (fsc == null) {
            return null;
        }

        Long accountBalance = null;
        if (fsc.getAccount() != null) {
            accountBalance = fsc.getAccount().getBalance();
        }

        String ownerName = null;
        String ownerLastname = null;
        String ownerSurname = null;
        if (fsc.getOwner() != null) {
            ownerName = fsc.getOwner().getName();
            ownerLastname = fsc.getOwner().getLastname();
            ownerSurname = fsc.getOwner().getSurname();
        }

        return new FSCShortInfo(
                fsc.getId(),
                fsc.getName(),
                accountBalance,
                fsc.getFscType(),
                ownerName,
                ownerLastname,
                ownerSurname,
                fsc.getDisabled()
        );
    }
    public static FscInfoDto toFscInfoDto(FSC fsc) {
        if (fsc == null) {
            return null;
        }

        // Извлечение данных владельца
        String ownerFullName = null;
        String ownerEmail = null;
        Boolean ownerDisabled = null;
        UserRole ownerRole = null;
        Boolean fscDisabled = null;

        if (fsc.getOwner() != null) {
            User owner = fsc.getOwner();
            ownerFullName = String.join(" ", owner.getName(), owner.getLastname(), owner.getSurname() != null ? owner.getSurname() : "");
            ownerEmail = owner.getEmail();
            ownerDisabled = owner.getDisabled();
            ownerRole = owner.getRole();
            fscDisabled = fsc.getDisabled();
        }

        // Извлечение данных счета
        String accountNumber = null;
        Long balance = null;

        if (fsc.getAccount() != null) {
            Account account = fsc.getAccount();
            accountNumber = account.getAccountNumber();
            balance = account.getBalance();
        }

        // Создание и возвращение объекта CfoInfo
        return new FscInfoDto(
                fsc.getId(),               // Идентификатор ЦФО
                fsc.getName(),             // Название ЦФО
                accountNumber,             // Номер счета ЦФО
                ownerFullName,             // Полное имя владельца
                ownerEmail,                // Электронная почта владельца
                ownerDisabled,
                fscDisabled,
                ownerRole,                 // Роль владельца
                balance,                   // Баланс счета
                fsc.getFscType()           // Тип ЦФО
        );
    }

}
