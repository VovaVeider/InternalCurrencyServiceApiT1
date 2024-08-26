package org.vladimir.t1.currency.service.api.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vladimir.t1.currency.service.api.dto.ErrorResponse;
import org.vladimir.t1.currency.service.api.exception.ApiException;

import java.io.IOException;

@Slf4j
public class ErrorHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("ErrorHandlerFilter call doFilter(). Request URI: {} Method: {}", request.getRequestURI(), request.getMethod());
            filterChain.doFilter(request, response);
            log.info("ErrorHandlerFilter return from doFilter(). Request URI: {} Method: {}", request.getRequestURI(), request.getMethod());
        } catch (ApiException ae) {
            response.setContentType("application/json");
            log.error("ErrorHandlerFilter handle error. Class: {} , Type: {}, Reason{}, Description: {}",
                    ae.getClass().getSimpleName(), ae.getType(), ae.getReason(), ae.getDescription());
            response.setStatus(ae.getHttpStatus().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(ae.getType(), ae.getMessage(), ae.getDescription())));
        }
    }
}
