package com.premiumminds.webapp.wicket.testing;

import org.apache.wicket.Component;
import org.apache.wicket.markup.IMarkupFragment;

/**
 * Holds the component which is passed to {@link ExtendedWicketTester}'s
 * {@link ExtendedWicketTester#startComponentInForm()} methods and meta data about it.
 */
class ComponentInForm {
	/**
	 * The component id that is used when Wicket instantiates the tested/started component.
	 */
	static final String ID = "testObject";

	/**
	 * The component that is being started.
	 */
	Component component;

	/**
	 * A flag indicating whether the {@link #component} has been instantiated by Wicket.
	 * 
	 * @see {@link ExtendedWicketTester#startComponentInForm(Class, IMarkupFragment)}.
	 */
	boolean isInstantiated = false;
}
