package org.vladimir.t1.currency.service.api.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.auth.AccessToken;
import org.vladimir.t1.currency.service.api.auth.LoginAndRegistrationAuthentication;
import org.vladimir.t1.currency.service.api.auth.RefreshToken;
import org.vladimir.t1.currency.service.api.auth.Tokens;
import org.vladimir.t1.currency.service.api.auth.serialzer.AccessTokenJwsStringSerializer;
import org.vladimir.t1.currency.service.api.auth.serialzer.RefreshTokenJweStringSerializer;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j // Аннотация для автоматического создания логгера
@RequiredArgsConstructor
public class GetJwtFilter extends OncePerRequestFilter {
    private final List<AntPathRequestMatcher> includedPaths =
            List.of(new AntPathRequestMatcher("/users", "POST"),
                    new AntPathRequestMatcher("/users/me/login", "POST"));

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AccessTokenJwsStringSerializer accessSerializer;
    private final RefreshTokenJweStringSerializer refreshSerializer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Processing request for URI: {}", request.getRequestURI());

        if (includedPaths.stream().anyMatch(requestMatcher -> requestMatcher.matches(request))) {
            log.debug("Request matches included paths, proceeding with filter chain.");

            filterChain.doFilter(request, response);

            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                log.debug("User authenticated, generating tokens.");

                LoginAndRegistrationAuthentication authentication = (LoginAndRegistrationAuthentication) auth;
                UUID id = UUID.randomUUID();
                var accessToken = new AccessToken(id, authentication.getPrincipal(), authentication.getAuthorities().stream().findAny().get().toString(), Instant.now(), Instant.now().plus(Duration.ofMinutes(40)));
                var refreshToken = new RefreshToken(id, authentication.getPrincipal(), Instant.now(), Instant.now().plus(Duration.ofDays(1)));

                response.setContentType("application/json");
                String tokensJson = objectMapper.writeValueAsString(new Tokens(
                        accessSerializer.apply(accessToken),
                        refreshSerializer.apply(refreshToken)
                ));
                response.getWriter().write(tokensJson);

                log.debug("Tokens generated and written to response: {}", tokensJson);
            } else {
                log.debug("No authentication found in security context.");
            }
        } else {
            log.debug("Request does not match included paths, continuing filter chain.");
            filterChain.doFilter(request, response);
        }
    }
}
