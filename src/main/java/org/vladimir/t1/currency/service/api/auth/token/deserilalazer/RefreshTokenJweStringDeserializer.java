package org.vladimir.t1.currency.service.api.auth.token.deserilalazer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import lombok.extern.slf4j.Slf4j;
import org.vladimir.t1.currency.service.api.auth.token.RefreshToken;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;
@Slf4j
public class RefreshTokenJweStringDeserializer implements Function<String, RefreshToken> {

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweStringDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }

    @Override
    public RefreshToken apply(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimsSet = encryptedJWT.getJWTClaimsSet();
            return new RefreshToken(
                    UUID.fromString(claimsSet.getJWTID()),
                    Long.parseLong(claimsSet.getSubject()),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());

        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }
}
