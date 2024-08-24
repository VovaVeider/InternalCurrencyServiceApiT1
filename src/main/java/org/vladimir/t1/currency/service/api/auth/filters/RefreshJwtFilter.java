package org.vladimir.t1.currency.service.api.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.auth.BlockedTokenStorage;
import org.vladimir.t1.currency.service.api.auth.token.RefreshToken;
import org.vladimir.t1.currency.service.api.auth.token.TokensFabric;
import org.vladimir.t1.currency.service.api.auth.token.deserilalazer.RefreshTokenJweStringDeserializer;
import org.vladimir.t1.currency.service.api.exception.token.TokenException;
import org.vladimir.t1.currency.service.api.exception.token.TokenExceptionType;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
public class RefreshJwtFilter extends OncePerRequestFilter {

    AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/users/me/refresh-token", "POST");

    private final UserService userService;
    private final RefreshTokenJweStringDeserializer refreshTokenDeserializer;
    private final TokensFabric tokensFabric;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BlockedTokenStorage blockedTokenStorage;

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

            var userId = refreshToken.userId();
            var userInfo = userService.getUserInfo(userId);

            if (userInfo == null)
                throw new TokenException(TokenExceptionType.TOKEN_BLOCKED, "User not found");
            if (userInfo.disabled())
                throw new TokenException(TokenExceptionType.TOKEN_BLOCKED, "User is blocked");


            var tokensPair = tokensFabric.createTokens(userId, userInfo.role());
            var tokensJson = objectMapper.writeValueAsString(tokensPair);

            response.setContentType("application/json");
            response.getWriter().write(tokensJson);

        } else {
            filterChain.doFilter(request, response);
        }
    }

}
