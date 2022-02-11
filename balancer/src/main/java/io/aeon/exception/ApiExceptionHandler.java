package io.aeon.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@ExceptionHandler(value = ApiException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionMessage handleApException(ApiException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		log.error(ex.getMessage());
		return new ExceptionMessage(status, ex.getMessage(), status.value());
	}
	
	@ExceptionHandler(ServiceDiscoveryException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ExceptionMessage handleRuntimeException(ServiceDiscoveryException ex) {
		HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
		log.error(ex.getMessage());
		return new ExceptionMessage(status, ex.getMessage(), status.value());
	}
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionMessage handleRuntimeException(RuntimeException ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		log.error(ex.getMessage());
		return new ExceptionMessage(status, ex.getMessage(), status.value());
	}
}
