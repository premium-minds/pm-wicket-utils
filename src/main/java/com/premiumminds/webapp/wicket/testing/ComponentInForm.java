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
