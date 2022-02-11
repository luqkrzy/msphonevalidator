package io.aeon.exception;

import io.aeon.api.ApiException;
import io.aeon.api.ExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<io.aeon.api.ExceptionMessage> handleApiException(ApiException ex) {
		io.aeon.api.ExceptionMessage message = new io.aeon.api.ExceptionMessage(ex.getStatus(), ex.getMessage(),
																				ex.getStatus().value());
		log.warn(ex.getMessage());
		return new ResponseEntity<>(message, ex.getStatus());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<io.aeon.api.ExceptionMessage> handleRuntimeException(RuntimeException ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		io.aeon.api.ExceptionMessage message = new ExceptionMessage(status, ex.getMessage(), status.value());
		log.error(ex.getMessage());
		return new ResponseEntity<>(message, status);
	}
}
