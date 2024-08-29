package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "payment_purposes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PaymentPurpose {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @Enumerated(EnumType.ORDINAL)
  @Column(nullable = false,name = "payment_purpose_type")
  private PaymentPurposeType type;

}