package io.aeon.ocrparser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping()
record OcrParserController(OcrParserService service) {

    @PostMapping("/ocr")
    ApiResponse doOcr(@Valid @RequestBody ApiRequest apiRequest, HttpServletRequest request) {
        log.info("Request received from: " + request.getRemoteAddr() + request.getRemotePort() + " processing...");
        return service.doOCR(apiRequest);
    }

    @GetMapping("/test")
    void test(HttpServletRequest request) {
        System.out.println("test endpoint " + request.getRemoteAddr());
    }
}
