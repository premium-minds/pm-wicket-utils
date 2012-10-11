package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;

public class ObjectChoiceControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = -8444849747715611613L;

	private DropDownChoice<T> dropDown;
	
	public ObjectChoiceControlGroup(String id, IModel<T> model) {
		super(id, model);

		dropDown = new DropDownChoice<T>("input", getModel(), (IModel<? extends List<? extends T>>) new LoadableDetachableModel<List<T>>() {
			private static final long serialVersionUID = -3995535290067544541L;

			@SuppressWarnings("unchecked")
			@Override
			protected List<T> load() {
				return (List<T>) getConfiguration().getProviders().get(getPropertyName()).load();
			}
		});
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new BootstrapControlGroupFeedback("controlGroup").add(dropDown));
	}

	@Override
	public FormComponent<T> getFormComponent() {
		return dropDown;
	}
}
