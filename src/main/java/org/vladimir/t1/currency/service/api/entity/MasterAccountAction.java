package org.vladimir.t1.currency.service.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "MASTER_ACCOUNT_ACTIONS")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class MasterAccountAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long amount;
    Instant time;

}
