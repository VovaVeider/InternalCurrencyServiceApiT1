package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "payment_purposes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentPurpose {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

}