package io.aeon.ocrparser;

import io.aeon.exception.ApiException;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OcrParserServiceTest {

    @Autowired
    private OcrParserService service;

    @ParameterizedTest(name = "[{index}] expect={1}")
    @CsvFileSource(resources = "/correctBase64.csv", maxCharsPerColumn = 90000)
    void test_doOCR_properApiRequestData_returnsTrue(String base64, String expected) {
        ApiRequest apiRequest = new ApiRequest(base64);
        ApiResponse apiResponse = service.doOCR(apiRequest);
        assertNotNull(apiRequest);
        assertEquals(apiResponse.ocr(), expected);
    }

    @ParameterizedTest()
    @CsvFileSource(resources = "/wrongBase64.csv", maxCharsPerColumn = 90000)
    void test_doOCR_wrongApiRequestData_throwsException(String base64) {
        ApiRequest apiRequest = new ApiRequest(base64);
        assertNotNull(apiRequest);
        assertThrows(ApiException.class, () -> service.doOCR(apiRequest));
    }
}
