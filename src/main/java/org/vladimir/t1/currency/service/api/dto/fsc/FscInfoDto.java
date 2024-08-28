package org.vladimir.t1.currency.service.api.dto.fsc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.vladimir.t1.currency.service.api.entity.FscType;
import org.vladimir.t1.currency.service.api.entity.UserRole;
@AllArgsConstructor
@Builder
@Setter
@Getter
public class FscInfoDto {
    private Long id; // Идентификатор ЦФО
    private String name; // Название ЦФО
    private String accountNumber; // Счёт ЦФО
    private String ownerFullName; // ФИО владельца
    private String ownerEmail; // Электронная почта владельца
    private Boolean ownerDisabled;
    private Boolean fscDisabled;// Статус "disabled" владельца
    private UserRole ownerRole; // Роль владельца
    private Long balance; // Баланс
    private FscType fscType; // Тип ЦФО
}
