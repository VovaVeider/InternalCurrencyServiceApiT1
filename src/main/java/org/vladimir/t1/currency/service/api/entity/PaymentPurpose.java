package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;


@Entity
@Table(name = "payment_purposes")
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentPurpose {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, nullable = false)
  private String name;

}