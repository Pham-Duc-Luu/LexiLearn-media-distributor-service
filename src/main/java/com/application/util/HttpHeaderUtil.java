package com.application.util;


import com.application.dto.HttpErrorDto;
import com.application.service.AccessTokenJwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpHeaderUtil {
    Logger logger = LogManager.getLogger(HttpHeaderUtil.class);
    @Autowired
    AccessTokenJwtService accessTokenJwtService;


    public String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    public String extractBearerFromAuthenticationList(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }

        for (String token : tokens) {
            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7); // Remove "Bearer " prefix
            }
        }
        return null;
    }

    // Convert HttpErrorDto to JSON string (you could use a library like Jackson or Gson for this)
    public String errorResponseToJson(HttpErrorDto errorResponse) {
        return "{" +
                "\"timestamp\":\"" + errorResponse.getTimestamp() + "\"," +
                "\"status\":" + errorResponse.getStatus() + "," +
                "\"error\":\"" + errorResponse.getError() + "\"," +
                "\"message\":\"" + errorResponse.getMessage() + "\"," +
                "\"path\":\"" + errorResponse.getPath() + "\"" +
                "}";
    }
}
