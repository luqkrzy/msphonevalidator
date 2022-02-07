package io.aeon.balancer;

import io.aeon.exception.ApiException;
import io.aeon.exception.ApiExceptionHandler;
import io.aeon.exception.ExceptionMessage;
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
record BalancerController(WebClient.Builder balancerWebClientBuilder) {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@RequestMapping("/ocr")
	public Mono<ApiResponse> doOcr(@RequestBody ApiRequest request, HttpServletRequest req) {
		log.info("Received request " + req.getRemoteAddr() + ":" + req.getRemotePort() + " processing...");
		return balancerWebClientBuilder.build().post().uri("http://ocr-parser/ocr")
									   .body(Mono.just(request), ApiRequest.class).retrieve()
									   .onStatus(HttpStatus::isError,
												 response -> response.bodyToMono(ExceptionMessage.class).flatMap(
														 error -> Mono.error(new ApiException(error.message()))))
									   .bodyToMono(ApiResponse.class);
	}
}
