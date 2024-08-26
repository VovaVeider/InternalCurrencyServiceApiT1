package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_number", referencedColumnName = "account_number")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_number", referencedColumnName = "account_number")
    private Account toAccount;

    private Instant time;

    private Long amount;

    private String paymentComment;

    @ManyToOne
    @JoinColumn(name = "payment_purpose")
    private PaymentPurpose paymentPurpose;

}
