package com.premiumminds.webapp.wicket.bootstrap.crudifier.form;

import java.io.Serializable;

public class CrudifierFormSettings implements Serializable {
	private static final long serialVersionUID = 4151791520551489660L;

	private boolean withSelfFeedback = true;

	public boolean isWithSelfFeedback() {
		return withSelfFeedback;
	}
	public void setWithSelfFeedback(boolean withSelfFeedback) {
		this.withSelfFeedback = withSelfFeedback;
	}
}
