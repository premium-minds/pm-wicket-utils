package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.CrudifierSettings;

public abstract class ButtonColumn<T extends Serializable> implements IColumn<T>, Serializable {
	private static final long serialVersionUID = 2920108073341698814L;

	private String propertyName;
	
	public ButtonColumn(String propertyName){
		this.propertyName = propertyName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public Component createComponent(String id, final T object,
			Component resourceBase, CrudifierSettings settings) {
		ButtonPanel panel = new ButtonPanel(id);
		panel.add(new AjaxLink<Void>("button") {
			private static final long serialVersionUID = 4260049524761483954L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ButtonColumn.this.onClick(Model.of(object), target);
			}
		});
		return panel;
	}

	public ColumnAlign getAlign() {
		return ColumnAlign.CENTER;
	}

	public abstract void onClick(IModel<T> model, AjaxRequestTarget target);
	
	public static class ButtonPanel extends Panel {
		private static final long serialVersionUID = -7728471050634320252L;

		public ButtonPanel(String id) {
			super(id);
		}
	}
}
