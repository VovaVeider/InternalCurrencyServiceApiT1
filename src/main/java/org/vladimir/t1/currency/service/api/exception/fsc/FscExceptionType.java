package org.vladimir.t1.currency.service.api.exception.fsc;

public enum FscExceptionType {
    //Ошибки создания ЦФО
    NAME_ALREADY_EXISTS,
    USER_HAS_NO_FSC_OWNER_RIGHT,
    STORE_FSC_HAVE_NO_INIT_BALANCE,
    MASTER_FSC_NO_ENOUGH_FUNDS,
    NEGATIVE_INIT_BALANCE,
    MASTER_FSC_CREATE_NOT_PERMITTED,
    FSC_OWNER_NOT_FOUND,
    FATAL_MASTER_ACCOUNT_NOT_FOUND,
    FSC_NOT_FOUND
}
