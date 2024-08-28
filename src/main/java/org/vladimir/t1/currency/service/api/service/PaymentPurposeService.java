package org.vladimir.t1.currency.service.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vladimir.t1.currency.service.api.dto.transaction.PaymentPurposeDto;
import org.vladimir.t1.currency.service.api.mapper.PaymentPurposeMapper;
import org.vladimir.t1.currency.service.api.repository.PaymentPurposeRepository;

import java.util.List;

import static org.vladimir.t1.currency.service.api.entity.PaymentPurposeType.*;

@Service
@RequiredArgsConstructor
public class PaymentPurposeService {
    private final PaymentPurposeRepository paymentPurposeRepository;

    public List<PaymentPurposeDto> getUserToUserPaymentPurposes() {
        return paymentPurposeRepository.findByTypeIn(List.of(USER_TO_USER, GENERAL))
                .stream()
                .map(PaymentPurposeMapper::map)
                .toList();
    }

    public List<PaymentPurposeDto> getFscToFscPaymentPurposes() {
        return paymentPurposeRepository.findByTypeIn(List.of(FSC_TO_FSC, GENERAL))
                .stream()
                .map(PaymentPurposeMapper::map)
                .toList();
    }

    public List<PaymentPurposeDto> getFscToUserPaymentPurposes() {
        return paymentPurposeRepository.findByTypeIn(List.of(FSC_TO_USER, GENERAL))
                .stream()
                .map(PaymentPurposeMapper::map)
                .toList();
    }
}
