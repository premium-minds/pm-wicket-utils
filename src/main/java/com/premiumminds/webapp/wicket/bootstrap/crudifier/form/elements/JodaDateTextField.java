package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

public class JodaDateTextField extends TextField<ReadableInstant> implements ITextFormatProvider {
	private static final long serialVersionUID = 1L;
	private String pattern;
	private IConverter<DateTime> converter;

	private static final String DEFAULT_PATTERN = "dd/MM/yyyy";

	public JodaDateTextField(String id) {
		this(id, null, defaultDatePattern());
	}

	public JodaDateTextField(String id, IModel<ReadableInstant> model) {
		this(id, model, defaultDatePattern());
	}

	public JodaDateTextField(String id, IModel<ReadableInstant> model,
			String pattern) {
		super(id, model, ReadableInstant.class);
		this.pattern = pattern;
		converter = new IConverter<DateTime>() {
			private static final long serialVersionUID = 1L;

			public String convertToString(DateTime value, Locale locale) {
				if(null == locale) {
					return DateTimeFormat.forPattern(JodaDateTextField.this.pattern).print(value);
				} else {
					return DateTimeFormat.forPattern(JodaDateTextField.this.pattern).withLocale(locale).print(value);
				}
			}

			public DateTime convertToObject(String value, Locale locale)
					throws ConversionException {
				if(null == locale) {
					return DateTimeFormat.forPattern(JodaDateTextField.this.pattern).parseDateTime(value);
				} else {
					return DateTimeFormat.forPattern(JodaDateTextField.this.pattern).withLocale(locale).parseDateTime(value);
				}
			}
		};
	}

	/**
	 * Returns the default converter if created without pattern; otherwise it returns a
	 * pattern-specific converter.
	 * 
	 * @param type
	 *            The type for which the convertor should work
	 * 
	 * @return A pattern-specific converter
	 * 
	 * @see org.apache.wicket.markup.html.form.TextField
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C> IConverter<C> getConverter(final Class<C> type)
	{
		if (Date.class.isAssignableFrom(type))
		{
			return (IConverter<C>)converter;
		}
		else
		{
			return super.getConverter(type);
		}
	}

	/**
	 * Returns the date pattern.
	 * 
	 * @see org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider#getTextFormat()
	 */
	@Override
	public String getTextFormat()
	{
		return pattern;
	}

	/**
	 * Try to get datePattern from user session locale. If it is not possible, it will return
	 * {@link #DEFAULT_PATTERN}
	 * 
	 * @return date pattern
	 */
	private static String defaultDatePattern()
	{
		return DEFAULT_PATTERN;
	}
}
