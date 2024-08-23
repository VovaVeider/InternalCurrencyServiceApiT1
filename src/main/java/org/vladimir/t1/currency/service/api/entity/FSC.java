package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fsc")
public class FSC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Account account;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "fsc_type_id")
    @Enumerated(EnumType.ORDINAL)
    private FscType fscType;

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private User owner;

    @Builder.Default
    @Column(name = "disabled", nullable = false)
    private Boolean disabled = false;

    @Column(name = "service_id")
    private Integer serviceId;

}