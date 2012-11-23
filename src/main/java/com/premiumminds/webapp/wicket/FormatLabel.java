package com.premiumminds.webapp.wicket;

import java.text.MessageFormat;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class FormatLabel extends Label {
	private static final long serialVersionUID = 9039206115545313591L;
	
	public FormatLabel(String id, String label) {
		super(id, label);
	}

	public FormatLabel(String id, IModel<?> model) {
		super(id, model);
	}
	
	public FormatLabel(String id){
		super(id);
	}

	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if(getDefaultModel()==null) throw new RuntimeException("Model can't be null on label "+getPageRelativePath());
		String format = getString("format", getDefaultModel());
		MessageFormat messageFormat = new MessageFormat(format, getSession().getLocale());
		
		replaceComponentTagBody(markupStream, openTag, messageFormat.format(new Object[]{getDefaultModelObject()}));
	}
}
