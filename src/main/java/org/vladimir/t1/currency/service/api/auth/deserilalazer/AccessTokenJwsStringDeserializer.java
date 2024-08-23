package org.vladimir.t1.currency.service.api.auth.deserilalazer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.vladimir.t1.currency.service.api.auth.AccessToken;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;
@Slf4j
public class AccessTokenJwsStringDeserializer implements Function<String, AccessToken> {


    private final JWSVerifier jwsVerifier;

    public AccessTokenJwsStringDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public AccessToken apply(String string) {
        try {
            var signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(this.jwsVerifier)) {
                var claimsSet = signedJWT.getJWTClaimsSet();
                return new AccessToken(
                        UUID.fromString(claimsSet.getJWTID()),
                        Integer.parseInt(claimsSet.getSubject()),
                        claimsSet.getClaim("role").toString(),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }
        return null;
    }
}
