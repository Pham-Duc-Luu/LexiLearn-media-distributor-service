package com.application.filter;


import com.application.dto.HttpErrorDto;
import com.application.dto.authentication.UserJWTObject;
import com.application.enums.JwtClaims;
import com.application.service.AccessTokenJwtService;
import com.application.util.HttpHeaderUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;


@Component
@Order(2)
public class AccessTokenFilter implements Filter {
    // List of URL patterns to protect
    private final List<Pattern> protectedUrlPatterns = List.of(
            Pattern.compile("/api/v1/.*") // Protect all URLs under /api/v1/protected
    );
    private final List<Pattern> excludedUrlPatterns = List.of();
    private final AccessTokenJwtService accessTokenJwtService;
    Logger logger = LogManager.getLogger(AccessTokenFilter.class);
    @Autowired
    HttpHeaderUtil httpHeaderUtil;

    @Autowired
    public AccessTokenFilter(AccessTokenJwtService accessTokenJwtService) {
        this.accessTokenJwtService = accessTokenJwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        // Extract API key from the "x-api-key" header
        String token = httpHeaderUtil.extractTokenFromHeader(httpRequest);

        // Check if the requested URL matches any protected patterns
        boolean isProtected = protectedUrlPatterns.stream()
                .anyMatch(pattern -> pattern.matcher(httpRequest.getRequestURI()).matches());

        boolean isExclude = excludedUrlPatterns.stream()
                .anyMatch(pattern -> pattern.matcher(httpRequest.getRequestURI()).matches());

        if (isProtected) {
            if (!isExclude) {
                if (token == null) {
                    // Create a custom error response
                    HttpErrorDto errorResponse = new HttpErrorDto(
                            HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            "Missing access token",
                            httpRequest.getRequestURI()
                    );

                    // Set the response status to 401 Unauthorized
                    ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
                    // Set the content type to JSON
                    response.setContentType("application/json");
                    // Write the error response as JSON
                    try (PrintWriter writer = response.getWriter()) {
                        writer.write(httpHeaderUtil.errorResponseToJson(errorResponse));
                    }
                    // Log the error message
                    logger.error("Authentication failed: missing access token");
                    return;
                }

                try {
                    DecodedJWT decodedJWT = accessTokenJwtService.verify(token);
                    Claim userClaim = decodedJWT.getClaim(JwtClaims.User.getClaimName());
                    if (!userClaim.isNull()) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        UserJWTObject user = objectMapper.readValue(userClaim.asString(), UserJWTObject.class);

                        // Attach user details to the request attributes
                        httpRequest.setAttribute("user", userClaim.asString());
                    }

                } catch (JWTVerificationException e) {
                    // Create a custom error response
                    e.printStackTrace();
                    HttpErrorDto errorResponse = new HttpErrorDto(
                            HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                            e.getMessage(),
                            httpRequest.getRequestURI()
                    );

                    // Set the response status to 401 Unauthorized
                    httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    // Set the content type to JSON
                    response.setContentType("application/json");
                    // Write the error response as JSON
                    try (PrintWriter writer = response.getWriter()) {
                        writer.write(httpHeaderUtil.errorResponseToJson(errorResponse));
                    }
                    // Log the error message
                    logger.error("Authentication failed: {}", e.getMessage());
                    return;
                }
            }

        }
        // Proceed to the next filter in the chain
        chain.doFilter(request, response);

    }


}
