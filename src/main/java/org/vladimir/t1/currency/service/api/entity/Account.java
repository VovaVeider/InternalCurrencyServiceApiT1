package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Builder.Default
    @Column(nullable = false)
    private Boolean disabled = false;

    @Column(nullable = false, name = "user_account")
    private Boolean userAccount;

    @Column(nullable = false)
    private Integer balance;

}
