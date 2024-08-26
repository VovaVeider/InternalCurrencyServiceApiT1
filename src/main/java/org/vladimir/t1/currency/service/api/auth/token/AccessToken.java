package org.vladimir.t1.currency.service.api.auth.token;

import java.time.Instant;
import java.util.UUID;

public record AccessToken(UUID id, Long userId, String role, Instant created, Instant expires) {
}
