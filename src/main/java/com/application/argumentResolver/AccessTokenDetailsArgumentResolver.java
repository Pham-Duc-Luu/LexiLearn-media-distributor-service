package com.application.argumentResolver;

import com.application.dto.authentication.UserJWTObject;
import com.application.exception.HttpInternalServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Order(3)
@Component
public class AccessTokenDetailsArgumentResolver implements HandlerMethodArgumentResolver {
    Logger logger = LogManager.getLogger(AccessTokenDetailsArgumentResolver.class);


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Check if the parameter type is UserDetailsDto
        return UserJWTObject.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String user = request.getAttribute("user").toString();

            UserJWTObject userJWTObject = objectMapper.readValue(user, UserJWTObject.class);
            return userJWTObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpInternalServerErrorException();
        }
        // Extract user object from request attributes
//        UserJWTObject userJWTObject = (UserJWTObject) request.getAttribute("user");


//        if (userJWTObject == null) {
//            throw new IllegalArgumentException("Required user details not found in the request.");
//        }


    }
}