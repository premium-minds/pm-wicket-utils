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

/**
 * <tt>TestNotStartedException</tt> is thrown when {@link AbstractComponentTest} gets a call to
 * {@link AbstractComponentTest#getTarget()} before getting a call to
 * {@link AbstractComponentTest#startTest(Component)} or
 * {@link AbstractComponentTest#startTest(Component, boolean)}
 */
public class TestNotStartedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TestNotStartedException() {
		super("getTarget should not be called before startTest");
	}
}
