package org.vladimir.t1.currency.service.api.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.vladimir.t1.currency.service.api.auth.token.AccessToken;

import java.util.Collection;
import java.util.List;

public class TokenAuthentication implements Authentication {
    AccessToken accessToken;
    Boolean isAuthenticated = false;
    public TokenAuthentication(AccessToken token) {
        accessToken = token;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(accessToken.role()));
    }

    @Override
    public AccessToken getCredentials() {
        return accessToken;
    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public Integer getPrincipal() {
        return accessToken.userId();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return "";
    }
}
