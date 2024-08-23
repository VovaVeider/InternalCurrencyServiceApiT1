package org.vladimir.t1.currency.service.api.auth.serialzer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vladimir.t1.currency.service.api.auth.AccessToken;

import java.util.Date;
import java.util.function.Function;
@Slf4j

public class AccessTokenJwsStringSerializer implements Function<AccessToken, String> {

    private final JWSSigner jwsSigner;

    @Setter
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }

    @Override
    public String apply(AccessToken token) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.id().toString())
                .build();

        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(String.valueOf(token.userId()))
                .issueTime(Date.from(token.created()))
                .expirationTime(Date.from(token.expires()))
                .claim("role", token.role())
                .build();

        var signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }

}
