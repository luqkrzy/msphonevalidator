package pl.nettic.ocrparser;

import pl.nettic.api.ApiRequest;
import pl.nettic.api.ApiResponse;

interface OcrParserService {
	
	ApiResponse doOCR(ApiRequest request);
}
