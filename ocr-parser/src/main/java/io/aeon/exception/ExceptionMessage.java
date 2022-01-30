package io.aeon.exception;

import org.springframework.http.HttpStatus;

public record ExceptionMessage(HttpStatus httpStatus,
                               String message,
                               int code) {
}
