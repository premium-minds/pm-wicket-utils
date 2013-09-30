package com.premiumminds.webapp.wicket.bootstrap.crudifier.form.elements;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidationError;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;

public class TextFieldControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = -944776898493154174L;

	private TextField<T> textField;
	
	public TextFieldControlGroup(String id, IModel<T> model) {
		super(id, model);

		textField = new TextField<T>("input", getModel()){
			private static final long serialVersionUID = 4925601760084153117L;

			@Override
			public void error(IValidationError error) {
				MessageSource source = new MessageSource();
				Serializable message = error.getErrorMessage(source);
				
				super.error(message);
			}
		};
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		textField.add(AttributeModifier.replace("placeHolder", new StringResourceModel(getPropertyName()+".placeHolder", getResourceBase(), getModel(), "")));
		add(new BootstrapControlGroupFeedback("controlGroup").add(textField));
	}

	@Override
	public FormComponent<T> getFormComponent() {
		return textField;
	}
}
