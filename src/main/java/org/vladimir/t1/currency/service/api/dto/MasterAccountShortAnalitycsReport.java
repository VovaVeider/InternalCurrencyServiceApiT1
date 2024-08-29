package org.vladimir.t1.currency.service.api.dto;

public record MasterAccountShortAnalitycsReport(
        Long amountEntered,
        Long amountTransferedToFSC,
        Long amountWithdrawn
) {
}
