package io.aeon.ocrparser;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
record TesseractParser() {
	
	private static final String TESSDATA_PATH = "/tessdata";
	
	@Bean
	public Tesseract getTesseract() {
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath(System.getProperty("user.dir") + TESSDATA_PATH);
		return tesseract;
	}
}
