package com.premiumminds.webapp.wicket;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Time;

public abstract class StringDownloadResource extends AbstractResource {
	private static final long serialVersionUID = 7033429559494560378L;
	private String contentType;
	private String filename;
	
	public StringDownloadResource(String contentType, String filename){
		this.contentType = contentType;
		this.filename = filename;
	}
	
	protected abstract String getString();

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse response = new ResourceResponse();
		response.setContentType(contentType);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		response.setFileName(filename);
		response.setLastModified(Time.now());
		response.disableCaching();
		response.setWriteCallback(new WriteCallback() {
			
			@Override
			public void writeData(Attributes attributes) {
				attributes.getResponse().write(getString());
			}
		});
		return response;
	}

}
