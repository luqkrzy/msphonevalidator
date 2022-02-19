package pl.nettic.balancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import pl.nettic.api.ApiException;
import pl.nettic.api.ApiRequest;
import pl.nettic.api.ApiResponse;
import pl.nettic.api.ExceptionMessage;
import pl.nettic.exception.ApiExceptionHandler;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
record BalancerController(WebClient.Builder webClientBuilder) {
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	@RequestMapping("/ocr")
	public Mono<ApiResponse> doOcr(@Valid @RequestBody ApiRequest apiRequest, HttpServletRequest request) {
		log.info("Received apiRequest " + request.getRemoteAddr() + ":" + request.getRemotePort() + " processing...");
		return webClientBuilder.build().post().uri("http://ocr-parser/ocr")
							   .body(Mono.just(apiRequest), ApiRequest.class).retrieve()
							   .onStatus(HttpStatus::isError,
										 response -> response.bodyToMono(ExceptionMessage.class).flatMap(
												 error -> Mono.error(
														 new ApiException(error.message(), HttpStatus.BAD_REQUEST))))
							   .bodyToMono(ApiResponse.class);
	}
	
	@GetMapping("/ping")
	ApiResponse ping(HttpServletRequest request) {
		log.info("Ping received from: " + request.getRemoteAddr() + ":" + request.getRemotePort());
		return new ApiResponse("ping OK!");
	}
}
