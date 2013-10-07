package com.premiumminds.webapp.wicket.bootstrap;
import java.io.Serializable;
import java.util.Date;

public final class SpecialDate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date dt;
	private String cssClass, tooltip;

	public SpecialDate(Date dt, String cssClass, String tooltip) {
		this.dt = dt;
		this.cssClass = cssClass;
		this.tooltip = tooltip;
	}
	public Date getDt() {
		return dt;
	}
	public String getCssClass() {
		return cssClass;
	}
	public String getTooltip() {
		return tooltip;
	}
}