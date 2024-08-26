package org.vladimir.t1.currency.service.api.auth.token;

import lombok.RequiredArgsConstructor;
import org.vladimir.t1.currency.service.api.auth.token.serialzer.AccessTokenJwsStringSerializer;
import org.vladimir.t1.currency.service.api.auth.token.serialzer.RefreshTokenJweStringSerializer;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class TokensFabric {

    private final AccessTokenJwsStringSerializer accessTokenSerializer;
    private final RefreshTokenJweStringSerializer refreshTokenSerializer;
    private final Duration accessTokenTTL;
    private final Duration refreshTokenTTL;

    public Tokens createTokens(Long userId, String role) {
        var tokenPairId = UUID.randomUUID();
        Instant now = Instant.now();
        return new Tokens(
                accessTokenSerializer.apply(new AccessToken(tokenPairId, userId, role, now, now.plus(accessTokenTTL))),
                refreshTokenSerializer.apply(new RefreshToken(tokenPairId, userId, now, now.plus(refreshTokenTTL)))
        );
    }
}
