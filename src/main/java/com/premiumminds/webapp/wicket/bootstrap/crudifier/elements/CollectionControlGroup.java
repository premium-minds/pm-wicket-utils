package com.premiumminds.webapp.wicket.bootstrap.crudifier.elements;

import java.util.List;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapControlGroupFeedback;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;
import com.premiumminds.webapp.wicket.bootstrap.crudifier.EntityProvider;

@SuppressWarnings("rawtypes")
public class CollectionControlGroup<T> extends AbstractControlGroup<T> {
	private static final long serialVersionUID = 2580977991384659320L;

	private ListMultipleChoice multiChoice;
	
	@SuppressWarnings("unchecked")
	public CollectionControlGroup(String id, IModel<T> model) {
		super(id, model);
		
		multiChoice = new ListMultipleChoice("input", getModel(), new LoadableDetachableModel<List<T>>() {
			private static final long serialVersionUID = 3674039468142186197L;

			@Override
			protected List<T> load() {
				EntityProvider<?> provider = getConfiguration().getProviders().get(getPropertyName());
				if(provider==null) throw new RuntimeException("Provider for property "+getPropertyName()+" is missing, check your "+CrudifierSettings.class.getSimpleName());
				return (List<T>) getConfiguration().getProviders().get(getPropertyName()).load();
			}
		});
	}


	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new BootstrapControlGroupFeedback("controlGroup").add(multiChoice));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FormComponent<T> getFormComponent() {
		return multiChoice;
	}

}
