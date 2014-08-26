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
package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.util.Map;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapFeedbackPanel;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.elements.AbstractControlGroup;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.elements.ListControlGroups;

public class BootstrapStatelessCrudifierForm<T> extends StatelessForm<T> implements IBootstrapCrudifierForm<T> {
	private static final long serialVersionUID = -1762699420685191222L;

	private ListControlGroups<T> listControlGroups;
	
	public BootstrapStatelessCrudifierForm(String id, IModel<T> model, CrudifierSettings configuration) {
		super(id, model);

		setOutputMarkupId(true);
		
		add(new Label("legend", new StringResourceModel("legend", this, getModel(), "Unknown")){
			private static final long serialVersionUID = -7854751811138463187L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				setVisible(!getDefaultModelObjectAsString().isEmpty());
			}
		});

		if(configuration.isWithSelfFeedback()){
			add(new BootstrapFeedbackPanel("feedbackError", FeedbackMessage.ERROR).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackWarning", FeedbackMessage.WARNING).setEscapeModelStrings(false));
			add(new BootstrapFeedbackPanel("feedbackSuccess", FeedbackMessage.SUCCESS).setEscapeModelStrings(false));
		} else {
			add(new WebMarkupContainer("feedbackError").setVisible(false));
			add(new WebMarkupContainer("feedbackWarning").setVisible(false));
			add(new WebMarkupContainer("feedbackSuccess").setVisible(false));
		}
		
		add(listControlGroups = new ListControlGroups<T>("controls", getModel(), configuration));
		
		add(new Label("submitLabel", new StringResourceModel("submitLabel", this, getModel(), "Submit")));
		WebMarkupContainer reset = new WebMarkupContainer("reset");
		add(reset);
		reset.setVisible(configuration.isShowReset());
		reset.add(new Label("resetLabel", new StringResourceModel("resetLabel", this, getModel(), "Reset")));
	}
	
	@Override
	protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
		return new PanelMarkupSourcingStrategy(false);
	}

	public FormComponent<?> getFormComponent(String propertyName) {
		Map<String, AbstractControlGroup<?>> fields = listControlGroups.getFieldsControlGroup();
		if(!fields.containsKey(propertyName)) throw new RuntimeException("No property "+propertyName+" was found on the form");
		return fields.get(propertyName).getFormComponent();
	}
	
}
