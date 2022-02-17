package pl.nettic.api;

import org.springframework.http.HttpStatus;

public record ExceptionMessage(HttpStatus httpStatus, String message, int code) {
}
