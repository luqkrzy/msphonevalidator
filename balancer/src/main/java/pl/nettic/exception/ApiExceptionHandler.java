package pl.nettic.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.nettic.api.ApiException;
import pl.nettic.api.ExceptionMessage;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<pl.nettic.api.ExceptionMessage> handleApiException(ApiException ex) {
		pl.nettic.api.ExceptionMessage message = new pl.nettic.api.ExceptionMessage(ex.getStatus(), ex.getMessage(),
																					ex.getStatus().value());
		log.warn(ex.getMessage());
		return new ResponseEntity<>(message, ex.getStatus());
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<pl.nettic.api.ExceptionMessage> handleRuntimeException(RuntimeException ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		pl.nettic.api.ExceptionMessage message = new ExceptionMessage(status, ex.getMessage(), status.value());
		log.error(ex.getMessage());
		return new ResponseEntity<>(message, status);
	}
}
