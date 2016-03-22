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
package com.premiumminds.webapp.wicket.testing;

import static junit.framework.Assert.fail;

import java.lang.reflect.Constructor;

import javax.servlet.ServletContext;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.ContainerInfo;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.tester.WicketTester;

/**
 * A helper class to ease unit testing of Wicket form components without the need for a servlet
 * container. The class adds methods to start a component in a form, which have the same functionality as
 * {@link #startComponentInPage(Component)} and variants.
 */
public class ExtendedWicketTester extends WicketTester {
	private ComponentInForm componentInForm;

	/**
	 * Creates an <code>ExtendedWicketTester</code> and automatically creates a <code>WebApplication</code>,
	 * but the tester will have no home page.
	 */
	public ExtendedWicketTester()
	{
	}

	/**
	 * Creates an <code>ExtendedWicketTester</code> and automatically creates a <code>WebApplication</code>.
	 * 
	 * @param homePage
	 *            a home page <code>Class</code>
	 */
	public ExtendedWicketTester(final Class<? extends Page> homePage)
	{
		super(homePage);
	}

	/**
	 * Creates an <code>ExtendedWicketTester</code>.
	 * 
	 * @param application
	 *            a <code>WicketTester</code> <code>WebApplication</code> object
	 */
	public ExtendedWicketTester(final WebApplication application)
	{
		super(application);
	}

	/**
	 * Creates an <code>ExtendedWicketTester</code> to help unit testing.
	 * 
	 * @param application
	 *            a <code>WicketTester</code> <code>WebApplication</code> object
	 * @param path
	 *            the absolute path on disk to the web application's contents (e.g. war root) - may
	 *            be <code>null</code>
	 * 
	 * @see org.apache.wicket.mock.MockApplication#MockApplication()
	 */
	public ExtendedWicketTester(final WebApplication application, final String path)
	{
		super(application, path);
	}

	/**
	 * Creates an <code>ExtendedWicketTester</code> to help unit testing.
	 * 
	 * @param application
	 *            a <code>WicketTester</code> <code>WebApplication</code> object
	 * @param servletCtx
	 *            the servlet context used as backend
	 */
	public ExtendedWicketTester(WebApplication application, ServletContext servletCtx)
	{
		super(application, servletCtx);
	}

	/**
	 * Process a component. A web page will be automatically created with the markup created in
	 * {@link #createPageMarkup(String)}.
	 * <p>
	 *     <strong>Note</strong>: the instantiated component will have an auto-generated id. To
	 *     reach any of its children use their relative path to the component itself. For example
	 *     if the started component has a child a Link component with id "link" then after starting
	 *     the component you can click it with: <code>tester.clickLink("link")</code>
	 * </p>
	 * 
	 * @param <C>
	 *            the type of the component
	 * @param componentClass
	 *            the class of the component to be tested
	 * @param inputType
	 *            the type of HTML input to be associated with the component to be tested
	 * @return The component processed
	 * @see #startComponentInPage(org.apache.wicket.Component)
	 */
	public final <C extends Component> C startComponentInForm(final Class<C> componentClass, final String inputType)
	{
		return startComponentInForm(componentClass, inputType, null);
	}

	/**
	 * Process a component. A web page will be automatically created with the {@code pageMarkup}
	 * provided. In case pageMarkup is null, the markup will be automatically created with
	 * {@link #createFormPageMarkup(String, String)}.
	 * <p>
	 *     <strong>Note</strong>: the instantiated component will have an auto-generated id. To
	 *     reach any of its children use their relative path to the component itself. For example
	 *     if the started component has a child a Link component with id "link" then after starting
	 *     the component you can click it with: <code>tester.clickLink("link")</code>
	 * </p>
	 * 
	 * @param <C>
	 *            the type of the component
	 * 
	 * @param componentClass
	 *            the class of the component to be tested
	 * @param inputType
	 *            the type of HTML input to be associated with the component to be tested
	 * @param pageMarkup
	 *            the markup for the Page that will be automatically created. May be {@code null}.
	 * @return The component processed
	 */
	@SuppressWarnings("deprecation")
	public final <C extends Component> C startComponentInForm(final Class<C> componentClass,
		final String inputType, final IMarkupFragment pageMarkup)
	{
		Args.notNull(componentClass, "componentClass");

		// Create the component instance from the class
		C comp = null;
		try
		{
			Constructor<C> c = componentClass.getConstructor(String.class);
			comp = c.newInstance(ComponentInForm.ID);
			componentInForm = new ComponentInForm();
			componentInForm.component = comp;
			componentInForm.isInstantiated = true;
		}
		catch (Exception e)
		{
			fail(String.format("Cannot instantiate component with type '%s' because of '%s'",
				componentClass.getName(), e.getMessage()));
		}

		// process the component
		return startComponentInForm(comp, inputType, pageMarkup);
	}

	/**
	 * Process a component. A web page will be automatically created with markup created by the
	 * {@link #createFormPageMarkup(String, String)}.
	 * <p>
	 *     <strong>Note</strong>: the component id is set by the user. To
	 *     reach any of its children use this id + their relative path to the component itself. For example
	 *     if the started component has id <em>compId</em> and a Link child component component with id "link"
	 *     then after starting the component you can click it with: <code>tester.clickLink("compId:link")</code>
	 * </p>
	 * 
	 * @param <C>
	 *            the type of the component
	 * @param component
	 *            the component to be tested
	 * @param inputType
	 *            the type of HTML input to be associated with the component to be tested
	 * @return The component processed
	 * @see #startComponentInPage(Class)
	 */
	public final <C extends Component> C startComponentInForm(final C component, final String inputType)
	{
		return startComponentInForm(component, inputType, null);
	}

	/**
	 * Process a component. A web page will be automatically created with the {@code pageMarkup}
	 * provided. In case {@code pageMarkup} is null, the markup will be automatically created with
	 * {@link #createFormPageMarkup(String, String)}.
	 * <p>
	 *     <strong>Note</strong>: the component id is set by the user. To
	 *     reach any of its children use this id + their relative path to the component itself. For example
	 *     if the started component has id <em>compId</em> and a Link child component component with id "link"
	 *     then after starting the component you can click it with: <code>tester.clickLink("compId:link")</code>
	 * </p>
	 * 
	 * @param <C>
	 *            the type of the component
	 * @param component
	 *            the component to be tested
	 * @param inputType
	 *            the type of HTML input to be associated with the component to be tested
	 * @param pageMarkup
	 *            the markup for the Page that will be automatically created. May be {@code null}.
	 * @return The component processed
	 */
	@SuppressWarnings("deprecation")
	public final <C extends Component> C startComponentInForm(final C component,
		final String inputType, IMarkupFragment pageMarkup)
	{
		Args.notNull(component, "component");

		// Create a page object and assign the markup
		Page page = createFormPage();
		if (page == null)
		{
			fail("The automatically created page should not be null.");
		}

		// Automatically create the page markup if not provided
		if (pageMarkup == null)
		{
			String markup = createFormPageMarkup(component.getId(), inputType);
			if (markup == null)
			{
				fail("The markup for the automatically created page should not be null.");
			}

			try
			{
				// set a ContainerInfo to be able to use HtmlHeaderContainer so header contribution
				// still work. WICKET-3700
				ContainerInfo containerInfo = new ContainerInfo(page);
				MarkupResourceStream markupResourceStream = new MarkupResourceStream(
					new StringResourceStream(markup), containerInfo, page.getClass());

				MarkupParser markupParser = getApplication().getMarkupSettings()
					.getMarkupFactory()
					.newMarkupParser(markupResourceStream);
				pageMarkup = markupParser.parse();
			}
			catch (Exception e)
			{
				fail("Error while parsing the markup for the autogenerated page: " + e.getMessage());
			}
		}

		if (page instanceof StartComponentInForm)
		{
			((StartComponentInForm)page).setPageMarkup(pageMarkup);
		}
		else
		{
			page.setMarkup(pageMarkup);
		}

		// Add the child component
		Form<Void> form = new Form<Void>("form");
		page.add(form);
		form.add(component);

		// Preserve 'componentInForm' because #startPage() needs to null-fy it
		ComponentInForm oldComponentInForm = componentInForm;

		// Process the page
		startPage(page);

		// Remember the "root" component processes and return it
		if (oldComponentInForm != null)
		{
			componentInForm = oldComponentInForm;
		}
		else
		{
			componentInForm = new ComponentInForm();
			componentInForm.component = component;
		}
		return component;
	}

	/**
	 * Creates the markup that will be used for the automatically created {@link Page} that will be
	 * used to test a component with {@link #startComponentInPage(Class, IMarkupFragment)}
	 * 
	 * @param componentId
	 *            the id of the component to be tested
	 * @param inputType
	 * 			  the form input type ('text', 'checkbox', 'password', etc)
	 * @return the markup for the {@link Page} as {@link String}. Cannot be {@code null}.
	 */
	protected String createFormPageMarkup(final String componentId, final String inputType)
	{
		return /*"<html><head></head>" +*/ "<body><form wicket:id='form'><input wicket:id='" + componentId +
			"' type='" + inputType + "' /></form></body>" /*+ "</html>"*/;
	}

	/**
	 * Creates a {@link Page} to test a component with
	 * {@link #startComponentInForm(Component, String, IMarkupFragment)}
	 * 
	 * @return a {@link Page} which will contain a form as a single child with the component under
	 * test as a single input
	 */
	protected Page createFormPage()
	{
		return new StartComponentInForm();
	}

	/**
	 * A page that is used as the automatically created page for
	 * {@link ExtendedWicketTester#startComponentInForm(Class, String)} and the other variations.
	 * <p>
	 * This page caches the generated markup so that it is available even after
	 * {@link Component#detach()} where the {@link Component#markup component's markup cache} is
	 * cleared.
	 */
	public static class StartComponentInForm extends WebPage
	{
		private static final long serialVersionUID = 1L;

		private transient IMarkupFragment pageMarkup = null;

		/**
		 * Construct.
		 */
		public StartComponentInForm()
		{
			setStatelessHint(false);
		}

		@Override
		public IMarkupFragment getMarkup()
		{
			IMarkupFragment calculatedMarkup = null;
			if (pageMarkup == null)
			{
				IMarkupFragment markup = super.getMarkup();
				if (markup != null && markup != Markup.NO_MARKUP)
				{
					calculatedMarkup = markup;
					pageMarkup = markup;
				}
			}
			else
			{
				calculatedMarkup = pageMarkup;
			}

			return calculatedMarkup;
		}

		public void setPageMarkup(IMarkupFragment markup)
		{
			setMarkup(markup);
			pageMarkup = markup;
		}
	}
}
