package io.aeon.ocrparser;

import io.aeon.api.ApiException;
import io.aeon.api.ApiRequest;
import io.aeon.api.ApiResponse;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
record OcrParserServiceImpl(Tesseract tesseract) implements OcrParserService {
	
	private static final Logger log = LoggerFactory.getLogger(OcrParserServiceImpl.class);
	private static final String IMG_PATH = System.getProperty("user.dir") + "/tessdata/tmp";
	
	@Override
	public ApiResponse doOCR(ApiRequest request) {
		String filePath = IMG_PATH + UUID.randomUUID() + ".png";
		boolean isBase64 = Base64.isBase64(request.base64str());
		if (!isBase64) {
			String corrupted_data = "Corrupted data";
			log.error(corrupted_data);
			throw new ApiException(corrupted_data, HttpStatus.BAD_REQUEST);
		}
		try {
			byte[] decode = Base64.decodeBase64(request.base64str());
			saveFile(filePath, decode);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Couldn't read image");
		} catch (IllegalStateException | IllegalArgumentException e) {
			log.error(e.getMessage());
			throw new ApiException("Corrupted data", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException("Couldn't load data");
		}
		return doOCR(filePath);
	}
	
	private ApiResponse doOCR(String path) {
		String str;
		File file = new File(path);
		try {
			str = tesseract.doOCR(file);
			log.info("result: " + (!str.isBlank() ? str.trim() : "str is empty"));
			deleteFile(file);
		} catch (TesseractException e) {
			log.error(e.getMessage());
			deleteFile(file);
			throw new ApiException("Image doesn't contain valid alphanumeric text", HttpStatus.BAD_REQUEST);
		}
		String ocr = str.replace(System.lineSeparator(), "").strip();
		boolean valid = isValid(ocr);
		if (!valid) {
			throw new ApiException("Image doesn't contain valid numeric code", HttpStatus.BAD_REQUEST);
		}
		return new ApiResponse(ocr);
	}
	
	private void deleteFile(File imageFile) {
		if (imageFile.exists()) {
			imageFile.delete();
		}
	}
	
	private boolean isValid(String ocr) {
		if (!NumberUtils.isParsable(ocr.replaceAll("\\s+", ""))) {
			return false;
		}
		String[] array = ocr.split(" ");
		return Arrays.stream(array).allMatch(s -> s.length() == 6 || s.length() == 7);
	}
	
	private void saveFile(String filePath, byte[] decode) throws IOException {
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(decode);
		fos.close();
	}
}
