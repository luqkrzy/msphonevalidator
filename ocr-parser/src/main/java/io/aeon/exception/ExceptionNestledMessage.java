package io.aeon.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ExceptionNestledMessage(List<String> errors,
                                      HttpStatus httpStatus,
                                      int code) {
}
