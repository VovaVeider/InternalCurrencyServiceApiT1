package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false,name = "password")
    private String password;

    @Column(nullable = false)
    private Boolean disabled;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, name = "role_id")
    private UserRole role;

    @Column(nullable = false,name = "created_at")
    private Instant createdAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false,name = "account_id")
    Account account;

}
