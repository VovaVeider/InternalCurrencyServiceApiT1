package org.vladimir.t1.currency.service.api.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;

import io.micrometer.observation.Observation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.filter.CorsFilter;
import org.vladimir.t1.currency.service.api.auth.BlockedTokenStorage;
import org.vladimir.t1.currency.service.api.auth.deserilalazer.AccessTokenJwsStringDeserializer;
import org.vladimir.t1.currency.service.api.auth.deserilalazer.RefreshTokenJweStringDeserializer;
import org.vladimir.t1.currency.service.api.auth.filters.*;
import org.vladimir.t1.currency.service.api.auth.serialzer.AccessTokenJwsStringSerializer;
import org.vladimir.t1.currency.service.api.auth.serialzer.RefreshTokenJweStringSerializer;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    AccessTokenJwsStringSerializer accessTokenJwsStringSerializer(@Value("${jwt.access.secret}") String accessTokenKey) throws ParseException, KeyLengthException {
        return new AccessTokenJwsStringSerializer(new MACSigner(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer(@Value("${jwt.access.secret}") String accessTokenKey) throws ParseException, JOSEException {
        return new AccessTokenJwsStringDeserializer(new MACVerifier(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    RefreshTokenJweStringSerializer refreshTokenJweStringSerializer(@Value("${jwt.refresh.secret}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey)));
    }

    @Bean
    RefreshTokenJweStringDeserializer refreshTokenJweStringDeserializer(@Value("${jwt.refresh.secret}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringDeserializer(new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey)));
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
    public GetJwtFilter getJwtFilter(AccessTokenJwsStringSerializer accessTokenJwsStringSerializer,
                                     RefreshTokenJweStringSerializer refreshTokenJweStringDeserializer) {
        return new GetJwtFilter(accessTokenJwsStringSerializer, refreshTokenJweStringDeserializer);
    }


    @Bean
    public JwtLogoutFilter jwtLogoutFilter() {
        return new JwtLogoutFilter(blockedTokenStorage());
    }

    ;

    @Bean
    public RefreshJwtFilter refreshJwtFilter(UserService userService,
                                             RefreshTokenJweStringSerializer refreshTokenJweStringSerializer,
                                             RefreshTokenJweStringDeserializer refreshTokenJweStringDeserializer,
                                             AccessTokenJwsStringSerializer accessTokenJwsStringSerializer) {
        return new RefreshJwtFilter(userService, refreshTokenJweStringDeserializer, accessTokenJwsStringSerializer, refreshTokenJweStringSerializer, blockedTokenStorage());
    }

    ;

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
