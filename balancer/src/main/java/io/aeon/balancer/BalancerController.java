package io.aeon.balancer;

import io.aeon.api.ApiException;
import io.aeon.api.ApiRequest;
import io.aeon.api.ApiResponse;
import io.aeon.api.ExceptionMessage;
import io.aeon.exception.ApiExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

@RestController
record BalancerController(WebClient.Builder webClientBuilder) {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@RequestMapping("/ocr")
	public Mono<ApiResponse> doOcr(@RequestBody ApiRequest apiRequest, HttpServletRequest request) {
		log.info("Received apiRequest " + request.getRemoteAddr() + ":" + request.getRemotePort() + " processing...");
		return webClientBuilder.build().post().uri("http://ocr-parser/ocr")
							   .body(Mono.just(apiRequest), ApiRequest.class).retrieve()
							   .onStatus(HttpStatus::isError,
										 response -> response.bodyToMono(ExceptionMessage.class).flatMap(
												 error -> Mono.error(
														 new ApiException(error.message(), HttpStatus.BAD_REQUEST))))
									   .bodyToMono(ApiResponse.class);
	}
}
