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

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Locale;

import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemporalTextField<T extends Temporal> extends TextField<T> implements ITextFormatProvider {
	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(TemporalTextField.class);
	private static final long serialVersionUID = 1L;
	private String pattern;
	private IConverter<T> converter;

	private static final String DEFAULT_PATTERN = "dd/MM/yyyy";

	public TemporalTextField(String id, Class<T> type) {
		this(id, null, defaultDatePattern(), type);
	}

	public TemporalTextField(String id, IModel<T> model, Class<T> type) {
		this(id, model, defaultDatePattern(), type);
	}

	public TemporalTextField(String id, IModel<T> model,
			String pattern, final Class<T> type) {
		super(id, model, type);

		this.pattern = pattern;

		converter = new IConverter<T>() {
			private static final long serialVersionUID = 1L;

			public String convertToString(Temporal value, Locale locale) {
				if(null == locale) {
					return DateTimeFormatter.ofPattern(TemporalTextField.this.pattern).format(value);
				} else {
					return DateTimeFormatter.ofPattern(TemporalTextField.this.pattern).withLocale(locale).format(value);
				}
			}

			@SuppressWarnings("unchecked")
			public T convertToObject(String value, Locale locale) throws ConversionException {
				DateTimeFormatter formatter =
			            new DateTimeFormatterBuilder().appendPattern(TemporalTextField.this.pattern)
			            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
			            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
			            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
			            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
			            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
			            .parseDefaulting(ChronoField.YEAR_OF_ERA, ZonedDateTime.now().getYear())
			            .toFormatter();
				if (null != locale) {
					formatter = formatter.withLocale(locale);
				}

				try {
					Method parser = type.getMethod("parse", CharSequence.class, DateTimeFormatter.class);
					return (T)parser.invoke(null, value, formatter);
				} catch (NoSuchMethodException | SecurityException e1) {
					log.error("Could not find parser for type " + type.getSimpleName());
				} catch(Exception e) {
					log.error("Could not convert [" + value +"] to a valid " + type.getSimpleName());
				}

				return null;
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
		if (Temporal.class.isAssignableFrom(type))
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
