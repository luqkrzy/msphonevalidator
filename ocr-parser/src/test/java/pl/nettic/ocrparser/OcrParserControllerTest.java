package pl.nettic.ocrparser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.nettic.api.ApiResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OcrParserControllerTest {
	
	static final String OCR_ENDPOINT = "http://localhost:8000/ocr";
	
	@ParameterizedTest(name = "[{index}] expect={1}")
	@CsvFileSource(resources = "/correctBase64.csv", maxCharsPerColumn = 90000)
	void endpoint_ocr_properBase64_returnsCorrectResponse(String base64, String expected) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<>();
		map.put("base64str", base64);
		ResponseEntity<ApiResponse> response = restTemplate.postForEntity(OCR_ENDPOINT, map, ApiResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected, response.getBody().ocr());
	}
	
	@ParameterizedTest()
	@CsvFileSource(resources = "/wrongBase64.csv", maxCharsPerColumn = 90000)
	void endpoint_ocr_wrongBase64_returnsBadRequest(String base64) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<>();
		map.put("base64str", base64);
		HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () ->
				restTemplate.postForEntity(OCR_ENDPOINT, map, ApiResponse.class));
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
	}
	
	@Test
	void endpoint_noBody_returnsBadRequest() {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> map = new HashMap<>();
		map.put("base64str", null);
		HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () ->
				restTemplate.postForEntity(OCR_ENDPOINT, map, ApiResponse.class));
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
	}
}
