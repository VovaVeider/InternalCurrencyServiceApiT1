package org.vladimir.t1.currency.service.api.entity;

import lombok.Getter;


public enum SystemPaymentPurposes {
    SET_FSC_INIT_BALANCE(1L),
    TOP_FSC_BALANCE_FROM_MASTER(2L),
    USER_PURCHASE(3L);
    @Getter
    private final Long id;

    private SystemPaymentPurposes(Long id) {
        this.id = id;
    }
}
