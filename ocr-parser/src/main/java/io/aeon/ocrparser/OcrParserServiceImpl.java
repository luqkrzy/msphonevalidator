package io.aeon.ocrparser;

import io.aeon.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
record OcrParserServiceImpl(Tesseract tesseract) implements OcrParserService {

    private static final String IMG_PATH = System.getProperty("user.dir") + "/tmp";

    @Override
    public ApiResponse doOCR(ApiRequest request) {
        String filePath = IMG_PATH + UUID.randomUUID() + "." + request.type();
        boolean isBase64 = Base64.isBase64(request.base64str());
        if (!isBase64) {
            String corrupted_data = "Corrupted data";
            log.error(corrupted_data);
            throw new ApiException(corrupted_data);
        }
        try {
            byte[] decode = Base64.decodeBase64(request.base64str());
            saveFile(filePath, decode);
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            throw new ApiException("Corrupted data");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Couldn't load data");
        }
        return doOCR(filePath);
    }

    private ApiResponse doOCR(String path) {
        String str;
        try {
            File imageFile = new File(path);
            str = tesseract.doOCR(imageFile);
            log.info("result: " + (!str.isBlank() ? str.trim() : "str is empty"));
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (TesseractException e) {
            log.error(e.getMessage());
            throw new ApiException("Image doesn't contain valid alphanumeric text");
        }
        String ocr = str.replace(System.getProperty("line.separator"), "").strip();
        boolean valid = isValid(ocr);
        if (!valid) {
            throw new ApiException("Image doesn't contain valid numeric code");
        }
        return new ApiResponse(ocr) ;
    }

    private boolean isValid(String ocr) {
        if (!NumberUtils.isParsable(ocr.replaceAll("\\s+",""))) {
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
