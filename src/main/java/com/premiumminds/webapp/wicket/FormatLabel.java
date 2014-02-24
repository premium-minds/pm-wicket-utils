package com.premiumminds.webapp.wicket;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.beanutils.ConversionException;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class FormatLabel<T> extends Label implements IGenericComponent<T> {
	private static final long serialVersionUID = 9039206115545313591L;
	
	public FormatLabel(String id, IModel<T> model) {
		super(id, model);
	}
	
	public FormatLabel(String id){
		super(id);
	}
	
	@Override
	public <C> IConverter<C> getConverter(Class<C> type) {
		return new CustomConverter<C>();
	}

	private class CustomConverter<C> implements IConverter<C>{

		private static final long serialVersionUID = 6258191476048316073L;

		@Override
		public C convertToObject(String value, Locale locale) throws ConversionException {
			throw new ConversionException(value);
		}

		@Override
		public String convertToString(C value, Locale locale) {
			if(getDefaultModel()==null){
				throw new RuntimeException("Model can't be null on label "+getPageRelativePath());
			}
			String format = getString("format", getDefaultModel());
			MessageFormat messageFormat = new MessageFormat(format, getSession().getLocale());
			
			return messageFormat.format(new Object[]{getDefaultModelObject()});
		}
	}
	
	@SuppressWarnings("unchecked")
	public IModel<T> getModel() {
		return (IModel<T>) getDefaultModel();
	}

	public void setModel(IModel<T> model) {
		setDefaultModel(model);
	}

	public void setModelObject(T object) {
		setDefaultModelObject(object);
	}

	public T getModelObject() {
		return getModel().getObject();
	}
}
