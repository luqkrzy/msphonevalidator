package pl.nettic.ocrparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nettic.api.ApiRequest;
import pl.nettic.api.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping()
record OcrParserController(OcrParserService service) {
	
	private static final Logger log = LoggerFactory.getLogger(OcrParserController.class);
	
	@PostMapping("/ocr")
	ApiResponse doOcr(@Valid @RequestBody ApiRequest apiRequest, HttpServletRequest request) {
		log.info("Request received from: " + request.getRemoteAddr() + ":" + request.getRemotePort() + " processing...");
		return service.doOCR(apiRequest);
	}
}
