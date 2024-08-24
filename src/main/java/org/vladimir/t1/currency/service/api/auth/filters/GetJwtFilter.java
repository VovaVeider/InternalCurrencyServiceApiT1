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
import org.vladimir.t1.currency.service.api.auth.LoginAndRegistrationAuthentication;
import org.vladimir.t1.currency.service.api.auth.token.TokensFabric;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GetJwtFilter extends OncePerRequestFilter {

    private final List<AntPathRequestMatcher> includedPaths = List.of(
            new AntPathRequestMatcher("/users", "POST"),
            new AntPathRequestMatcher("/users/me/login", "POST")
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokensFabric tokensFabric;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("GetJwtFilter. Processing request for URI: {}", request.getRequestURI());
        filterChain.doFilter(request, response);

        if (includedPaths.stream().anyMatch(requestMatcher -> requestMatcher.matches(request))) {
            log.debug("GetJwtFilter. Request matches included paths, proceeding with filter chain.");
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                log.debug("GetJwtFilter. User authenticated, generating tokens.");

                LoginAndRegistrationAuthentication authentication = (LoginAndRegistrationAuthentication) auth;
                var tokensPair = tokensFabric.createTokens(authentication.getPrincipal(),authentication.getAuthorities().stream().findAny().get().toString());
                var tokensJson = objectMapper.writeValueAsString(tokensPair);

                response.setContentType("application/json");
                response.getWriter().write(tokensJson);

                log.debug("GetJwtFilter. Tokens generated and written to response: {}", tokensJson);
            } else {
                log.debug("GetJwtFilter. No authentication found in security context.");
            }
        }
    }
}
