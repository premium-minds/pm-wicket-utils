/**
 * Copyright (C) 2014 Premium Minds.
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

import java.util.Locale;

import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JodaInstantTextField<T extends ReadableInstant> extends TextField<T> implements ITextFormatProvider {
	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(JodaInstantTextField.class);
	private static final long serialVersionUID = 1L;
	private String pattern;
	private IConverter<T> converter;

	private static final String DEFAULT_PATTERN = "dd/MM/yyyy";

	public JodaInstantTextField(String id, Class<T> type) {
		this(id, null, defaultDatePattern(), type);
	}

	public JodaInstantTextField(String id, IModel<T> model, Class<T> type) {
		this(id, model, defaultDatePattern(), type);
	}

	public JodaInstantTextField(String id, IModel<T> model,
			String pattern, final Class<T> type) {
		super(id, model, type);
		this.pattern = pattern;
		converter = new IConverter<T>() {
			private static final long serialVersionUID = 1L;

			public String convertToString(ReadableInstant value, Locale locale) {
				if(null == locale) {
					return DateTimeFormat.forPattern(JodaInstantTextField.this.pattern).print(value);
				} else {
					return DateTimeFormat.forPattern(JodaInstantTextField.this.pattern).withLocale(locale).print(value);
				}
			}

			public T convertToObject(String value, Locale locale)
					throws ConversionException {
				try {
				if(null == locale) {
					return type.getConstructor(Long.TYPE).newInstance(DateTimeFormat.forPattern(JodaInstantTextField.this.pattern).parseDateTime(value).getMillis());
				} else {
					return type.getConstructor(Long.TYPE).newInstance(DateTimeFormat.forPattern(JodaInstantTextField.this.pattern).withLocale(locale).parseDateTime(value).getMillis());
				}
				} catch(Exception e) {
					log.error("Could not convert [" + value +"] to a valid " + type.getSimpleName());
					return null;
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
		if (ReadableInstant.class.isAssignableFrom(type))
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
