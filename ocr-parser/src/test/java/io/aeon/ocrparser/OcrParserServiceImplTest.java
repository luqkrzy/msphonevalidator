package io.aeon.ocrparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OcrParserServiceImplTest {

    private final String resourceDirectory = Paths.get("src", "test", "resources").toFile().getPath();

    @Autowired
    private OcrParserService service;

    @Test
    void test_doOCR_properApiRequestData_returnsTrue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<ApiRequest> correctRequests = objectMapper.readValue(new File(resourceDirectory + "/correctBase64.json"), objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ApiRequest.class));

        List<ApiResponse> correctData = objectMapper.readValue(new File(resourceDirectory + "/correctOcrParsedData.json"), objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ApiResponse.class));

        for (int i = 0; i < correctRequests.size(); i++) {
            ApiResponse resp = service.doOCR(correctRequests.get(i));
            assertEquals(correctData.get(i).ocr(), resp.ocr());
        }

    }
}
