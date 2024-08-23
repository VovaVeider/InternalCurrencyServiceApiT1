package org.vladimir.t1.currency.service.api.auth.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.auth.BlockedTokenStorage;
import org.vladimir.t1.currency.service.api.auth.TokenAuthentication;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final BlockedTokenStorage blockedTokenStorage;

    private final AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher("/users/me/logout", "POST");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (antPathMatcher.matches(request)) {
            TokenAuthentication tokenAuthentication = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
            blockedTokenStorage.block(tokenAuthentication.getCredentials().id());
        } else {
            filterChain.doFilter(request, response);
        }

    }
}
