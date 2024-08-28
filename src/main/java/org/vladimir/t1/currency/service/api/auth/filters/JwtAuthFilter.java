package org.vladimir.t1.currency.service.api.auth.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.auth.BlockedTokenStorage;
import org.vladimir.t1.currency.service.api.auth.TokenAuthentication;
import org.vladimir.t1.currency.service.api.auth.token.AccessToken;
import org.vladimir.t1.currency.service.api.auth.token.deserilalazer.AccessTokenJwsStringDeserializer;
import org.vladimir.t1.currency.service.api.exception.token.TokenException;
import org.vladimir.t1.currency.service.api.exception.token.TokenExceptionType;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AccessTokenJwsStringDeserializer deserializer;

    private final BlockedTokenStorage blockedTokenStorage;

    private final List<AntPathRequestMatcher> excludedPaths =
            List.of(new AntPathRequestMatcher("/users", "POST"),
                    new AntPathRequestMatcher("/users/me/login", "POST"),
                    new AntPathRequestMatcher("/users/me/refresh-token", "POST"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/swagger**"),
                    new AntPathRequestMatcher("/v3/api-docs/**"),
                    new AntPathRequestMatcher("/swagger.json/*")
            );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (excludedPaths.stream().noneMatch(requestMatcher -> requestMatcher.matches(request))) {
            var authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new TokenException(TokenExceptionType.INVALID_TOKEN,
                        "Invalid token was got", HttpStatus.UNAUTHORIZED);

            AccessToken accessToken = deserializer.apply(authHeader.replace("Bearer ", ""));

            if (accessToken == null)
                throw new TokenException(TokenExceptionType.INVALID_TOKEN, "Invalid token was got",
                        HttpStatus.UNAUTHORIZED);

            if (accessToken.expires().isBefore(Instant.now()))
                throw new TokenException(TokenExceptionType.ACCESS_TOKEN_OUTDATED,
                        "Access token expired.Get new via refresh token",
                        HttpStatus.UNAUTHORIZED);

            if (blockedTokenStorage.isBlocked(accessToken.id()))
                throw new TokenException(TokenExceptionType.TOKEN_BLOCKED,
                        "Token is blocked. Make new login and get new tokens", HttpStatus.UNAUTHORIZED);

            Authentication auth = new TokenAuthentication(accessToken);
            auth.setAuthenticated(true);
            SecurityContextHolder.setContext(new SecurityContextImpl(auth));
        }
        filterChain.doFilter(request, response);


    }
}
