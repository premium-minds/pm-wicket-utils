/**
 * Copyright (C) 2016 Premium Minds.
 *
 * This file is part of pm-wicket-utils.
 *
 * pm-wicket-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-wicket-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-wicket-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.wicket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;

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
		response.setLastModified(Instant.now());
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
