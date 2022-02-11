package io.aeon.api;

import org.springframework.http.HttpStatus;

public record ExceptionMessage(HttpStatus httpStatus, String message, int code) {
}
