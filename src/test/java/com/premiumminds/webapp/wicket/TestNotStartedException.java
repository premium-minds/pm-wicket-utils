package com.premiumminds.webapp.wicket;

public class TestNotStartedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TestNotStartedException() {
		super("getTarget should not be called before startTest");
	}
}
