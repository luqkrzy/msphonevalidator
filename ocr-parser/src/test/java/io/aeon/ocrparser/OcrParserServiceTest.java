package io.aeon.ocrparser;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OcrParserServiceTest {

    @Autowired
    private OcrParserService service;

    @ParameterizedTest(name = "[{index}] => type={0}, expect={2}")
    @CsvFileSource(resources = "/correctBase64.csv", maxCharsPerColumn = 90000)
    void test_doOCR_properApiRequestData_returnsTrue(String type, String base64, String expected) {
        ApiRequest apiRequest = new ApiRequest(type, base64);
        ApiResponse apiResponse = service.doOCR(apiRequest);
        assertNotNull(apiRequest);
        assertEquals(apiResponse.ocr(), expected);

    }
}
