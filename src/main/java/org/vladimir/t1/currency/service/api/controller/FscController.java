package org.vladimir.t1.currency.service.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.vladimir.t1.currency.service.api.dto.SingleFieldResponseDto;
import org.vladimir.t1.currency.service.api.dto.fsc.*;
import org.vladimir.t1.currency.service.api.dto.transaction.PaymentPurposeDto;
import org.vladimir.t1.currency.service.api.dto.user.TransactionReportDto;
import org.vladimir.t1.currency.service.api.entity.FscType;
import org.vladimir.t1.currency.service.api.exception.ApiException;
import org.vladimir.t1.currency.service.api.service.FscService;
import org.vladimir.t1.currency.service.api.service.PaymentPurposeService;

import java.util.List;

@RestController
@RequestMapping("fsc")
@RequiredArgsConstructor
public class FscController {
    private final FscService fscService;
    private final PaymentPurposeService paymentPurposeService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public SingleFieldResponseDto<List<FSCShortInfo>> getFscShortInfoList(@RequestParam(required = false) FscType fscType,
                                                                          @Min(1) int page,
                                                                          @Min(1) @Max(100) int size) {

        return new SingleFieldResponseDto<>(fscService.getFscShortInfoList(fscType, page - 1, size));
    }

    //@PreAuthorize()active-team-fsc
    @RequestMapping("active-team-fsc")
    public SingleFieldResponseDto<List<FscInfoDto>> getFscShortInfoList(
            @RequestParam(value = "name", required = true) String name,
            @Min(1) int page,
            @Min(1) @Max(100) int size) {
        return new SingleFieldResponseDto<>(fscService.getUserFscList(page - 1, size, name));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")

    public FscInfoDto getFscInfoById(@PathVariable Long id) {
        return fscService.getFscMainInfo(id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public CreateFscResponse createFsc(@RequestBody @Valid CreateFscRequest fscRequest) {
        return fscService.createFsc(fscRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_FSC_OWNER')")
    @GetMapping("/payments-purposes")
    public SingleFieldResponseDto<List<PaymentPurposeDto>> getFscPaymentPurposes(
            @RequestParam(value = "reciever", required = true) String reciever) {
        if (reciever == null)
            reciever = "";
        return switch (reciever) {
            case "user" -> new SingleFieldResponseDto<>(paymentPurposeService.getFscToUserPaymentPurposes());
            case "fsc" -> new SingleFieldResponseDto<>((paymentPurposeService.getFscToFscPaymentPurposes()));
            default -> throw new ApiException(
                    "ApiException",
                    "INCORRECT_REQUEST",
                    "Parameter paymentReciever allowed values: user, fsc"
            );
        };
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/top-up")
    public TransactionReportDto topUpFscAccount(@RequestBody TopUpFscRequest topUpFscRequest) {
        return fscService.topUpFscAccount(topUpFscRequest.id(), topUpFscRequest.amount());
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/transactions")
    public TransactionReportDto makeTransactionAsAdmin(@PathVariable(name = "id") Long fromFscId,
                                                       @RequestBody FscMakeTransaction fscMakeTransaction) {

        return fscService.makeTransaction(fromFscId,
                fscMakeTransaction.getToAccountNumber(),
                fscMakeTransaction.getAmount(),
                fscMakeTransaction.getPurposeId(),
                fscMakeTransaction.getPaymentComment());
    }


    @GetMapping("/types")
    public GetFstTypesResponse getFscTypes() {
        return new GetFstTypesResponse(List.of(FscType.TEAM.name(), FscType.STORE.name()));
    }


}
