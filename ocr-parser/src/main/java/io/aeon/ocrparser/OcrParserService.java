package io.aeon.ocrparser;

import io.aeon.api.ApiRequest;
import io.aeon.api.ApiResponse;

interface OcrParserService {
	
	ApiResponse doOCR(ApiRequest request);
}
