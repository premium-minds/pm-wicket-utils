package com.premiumminds.webapp.wicket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Time;

public class PDFResource extends AbstractResource {
	private static final long serialVersionUID = 8422342746704720791L;
	
	private PDFGeneratorCallback callback;

	public PDFResource(PDFGeneratorCallback callback){
		this.callback = callback;
		
	}
	
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse response = new ResourceResponse();
		
		response.setContentType("application/pdf");
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		response.setFileName(callback.getFilename());
		response.setLastModified(Time.now());
		response.disableCaching();
		response.setWriteCallback(new WriteCallback() {
			
			@Override
			public void writeData(Attributes attributes) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				callback.generatePDF(baos);
				
				attributes.getResponse().write(baos.toByteArray());
				
				try {
					baos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		return response;
	}
}
