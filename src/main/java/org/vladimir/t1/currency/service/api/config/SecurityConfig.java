package org.vladimir.t1.currency.service.api.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.vladimir.t1.currency.service.api.auth.BlockedTokenStorage;
import org.vladimir.t1.currency.service.api.auth.filters.*;
import org.vladimir.t1.currency.service.api.auth.token.TokensFabric;
import org.vladimir.t1.currency.service.api.auth.token.deserilalazer.AccessTokenJwsStringDeserializer;
import org.vladimir.t1.currency.service.api.auth.token.deserilalazer.RefreshTokenJweStringDeserializer;
import org.vladimir.t1.currency.service.api.auth.token.serialzer.AccessTokenJwsStringSerializer;
import org.vladimir.t1.currency.service.api.auth.token.serialzer.RefreshTokenJweStringSerializer;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.text.ParseException;
import java.time.Duration;
import java.util.List;

@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    AccessTokenJwsStringSerializer accessSerializer(@Value("${jwt.access.secret}") String accessTokenKey) throws ParseException, KeyLengthException {
        return new AccessTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    AccessTokenJwsStringDeserializer accessTokenDeserializer(@Value("${jwt.access.secret}") String accessTokenKey) throws ParseException, JOSEException {
        return new AccessTokenJwsStringDeserializer(new MACVerifier(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    RefreshTokenJweStringSerializer refreshTokenSerializer(@Value("${jwt.refresh.secret}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey)));
    }

    @Bean
    RefreshTokenJweStringDeserializer refreshTokenDeserializer(@Value("${jwt.refresh.secret}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey)));
    }

    @Bean
    TokensFabric tokensFabric(@Value("${jwt.access.ttl}") long accessTokenTTL,
                              @Value("${jwt.refresh.ttl}") long refreshTokenTTL,
                              AccessTokenJwsStringSerializer accessTokenSerializer,
                              RefreshTokenJweStringSerializer refreshTokenSerializer) {
        return new TokensFabric(accessTokenSerializer, refreshTokenSerializer,
                Duration.ofSeconds(accessTokenTTL), Duration.ofSeconds(refreshTokenTTL));
    }

    @Bean
    BlockedTokenStorage blockedTokenStorage() {
        return new BlockedTokenStorage();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer) {
        return new JwtAuthFilter(accessTokenJwsStringDeserializer, blockedTokenStorage());
    }

    @Bean
    public GetJwtFilter getJwtFilter(TokensFabric tokensFabric) {
        return new GetJwtFilter(tokensFabric);
    }


    @Bean
    public JwtLogoutFilter jwtLogoutFilter() {
        return new JwtLogoutFilter(blockedTokenStorage());
    }

    ;

    @Bean
    public RefreshJwtFilter refreshJwtFilter(UserService userService,
                                             RefreshTokenJweStringDeserializer refreshTokenJweStringDeserializer,
                                             TokensFabric tokensFabric) {

        return new RefreshJwtFilter(userService, refreshTokenJweStringDeserializer,
                tokensFabric, blockedTokenStorage()
        );
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http, JwtAuthFilter jwtAuthFilter,
                                         GetJwtFilter getJwtFilter, JwtLogoutFilter jwtLogoutFilter,
                                         RefreshJwtFilter refreshJwtFilter) throws Exception {
        return http
                .anonymous(AbstractHttpConfigurer::disable)         // AnonymousAuthenticationFilter
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())// CsrfFilter
                .sessionManagement(AbstractHttpConfigurer::disable) // DisableEncodeUrlFilter, SessionManagementFilter
                .exceptionHandling(AbstractHttpConfigurer::disable) // ExceptionTranslationFilter
                .headers(AbstractHttpConfigurer::disable)           // HeaderWriterFilter
                .logout(AbstractHttpConfigurer::disable)            // LogoutFilter
                .requestCache(AbstractHttpConfigurer::disable)      // RequestCacheAwareFilter
                .servletApi(AbstractHttpConfigurer::disable)        // SecurityContextHolderAwareRequestFilter
                .securityContext(AbstractHttpConfigurer::disable)
                .addFilterAfter(jwtAuthFilter, CorsFilter.class)
                .addFilterBefore(new ErrorHandlerFilter(), JwtAuthFilter.class)
                .addFilterAfter(jwtLogoutFilter, JwtAuthFilter.class)
                .addFilterAfter(refreshJwtFilter, JwtLogoutFilter.class)
                .addFilterAfter(getJwtFilter, RefreshJwtFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
