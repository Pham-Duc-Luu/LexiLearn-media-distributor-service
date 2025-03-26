package com.application.exception;

import com.application.dto.HttpErrorDto;
import org.springframework.http.HttpStatus;

public class HttpInternalServerErrorException extends HttpResponseException {
    public HttpInternalServerErrorException(String message) {
        super(new HttpErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                message
        ));
    }

    public HttpInternalServerErrorException() {
        super(new HttpErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
        ));
    }
}
