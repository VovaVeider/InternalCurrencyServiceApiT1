package org.vladimir.t1.currency.service.api.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.auth.*;
import org.vladimir.t1.currency.service.api.auth.deserilalazer.RefreshTokenJweStringDeserializer;
import org.vladimir.t1.currency.service.api.auth.serialzer.AccessTokenJwsStringSerializer;
import org.vladimir.t1.currency.service.api.auth.serialzer.RefreshTokenJweStringSerializer;
import org.vladimir.t1.currency.service.api.exception.token.TokenException;
import org.vladimir.t1.currency.service.api.exception.token.TokenExceptionType;
import org.vladimir.t1.currency.service.api.repository.UserRepository;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
public class RefreshJwtFilter extends OncePerRequestFilter {
    AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/users/me/refresh-token", "POST");

    private final UserService userService;


    private final RefreshTokenJweStringDeserializer refreshTokenDeserializer;

    private final AccessTokenJwsStringSerializer accessTokenSerializer;
    private final RefreshTokenJweStringSerializer refreshTokenSerializer;

    private  final BlockedTokenStorage blockedTokenStorage;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (antPathRequestMatcher.matches(request)) {
            var authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                throw new TokenException(TokenExceptionType.INVALID_TOKEN, "Invalid token was got");

            RefreshToken refreshToken = refreshTokenDeserializer.apply(authHeader.replace("Bearer ", ""));

            if (refreshToken == null)
                throw new TokenException(TokenExceptionType.INVALID_TOKEN, "Invalid token was got");

            if (refreshToken.expires().isBefore(Instant.now()))
                throw new TokenException(TokenExceptionType.REFRESH_TOKEN_OUTDATED, "Access token expired.Get new via refresh token");

            if (blockedTokenStorage.isBlocked(refreshToken.id()))
                throw new TokenException(TokenExceptionType.TOKEN_BLOCKED, "Token is blocked. Make new login and get new tokens");

            AccessToken token = getAccessToken(refreshToken.userId());
            if (token == null)
                throw new TokenException(TokenExceptionType.REFRESH_TOKEN_OUTDATED, "User doesnt found");

            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    new Tokens(
                            accessTokenSerializer.apply(token)
                           ,refreshTokenSerializer.apply(refreshToken))));

        } else {
            filterChain.doFilter(request,response);
        }
    }
    private AccessToken getAccessToken(Integer userId){
        var user =  userService.getUserInfo(userId);
        if (user == null)
                return null;
        return new AccessToken(UUID.randomUUID(),userId,user.role(),Instant.now(), Instant.now().plus(Duration.ofMinutes(40)));
    }
}
