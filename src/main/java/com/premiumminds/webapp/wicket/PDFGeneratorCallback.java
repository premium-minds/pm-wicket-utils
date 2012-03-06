package com.premiumminds.webapp.wicket;

import java.io.OutputStream;

public interface PDFGeneratorCallback {
	public void generatePDF(OutputStream outputStream);
	public String getFilename();
}
