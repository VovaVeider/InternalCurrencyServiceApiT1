package org.vladimir.t1.currency.service.api.auth.token;

import java.time.Instant;
import java.util.UUID;

public record RefreshToken (UUID id, int userId, Instant expires,Instant created){
}
