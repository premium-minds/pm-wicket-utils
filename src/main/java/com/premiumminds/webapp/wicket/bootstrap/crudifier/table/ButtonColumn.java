package com.premiumminds.webapp.wicket.bootstrap.crudifier.table;

import java.io.Serializable;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.premiumminds.webapp.wicket.bootstrap.crudifier.IObjectRenderer;

public abstract class ButtonColumn<T extends Serializable> implements IColumn<T>, Serializable {
	private static final long serialVersionUID = 2920108073341698814L;

	/**
	 * defines the button type. since this is a bootstrap implementation, see {@link http://getbootstrap.com/css/#buttons}
	 * for more information on each type
	 * @author cfraga
	 *
	 */
	public enum ButtonType {DEFAULT
		, PRIMARY
		, SUCCESS
		, INFO
		, WARNING
		, DANGER};
	private String propertyName;
		private ButtonType btnType;
	
		public ButtonColumn(String propertyName, ButtonType btnType){
		this.propertyName = propertyName;
			this.btnType = btnType;
		}

		public ButtonColumn(String propertyName){
			this(propertyName, ButtonType.DEFAULT);
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public Component createComponent(String id, final T object, Component resourceBase, Map<Class<?>, IObjectRenderer<?>> renderers) {
		ButtonPanel panel = new ButtonPanel(id);
		panel.add(new AjaxLink<Void>("button") {
			private static final long serialVersionUID = 4260049524761483954L;

				{
					add(AttributeModifier.append("class", getCssClass()));
				}
			@Override
			public void onClick(AjaxRequestTarget target) {
				ButtonColumn.this.onClick(Model.of(object), target);
			}
		}.add(new Label("label", resourceBase.getString(propertyName+".label", new Model<String>(), "Button"))));
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

		protected String getCssClass() {
			String rval = "btn ";
			switch(btnType) {
			case DANGER:
				rval += "btn-danger";
				break;
			case INFO:
				rval += "btn-info";
				break;
			case PRIMARY:
				rval += "btn-primary";
				break;
			case SUCCESS:
				rval += "btn-success";
				break;
			case WARNING:
				rval += "btn-warning";
				break;
			default:
				rval += "btn-default";
				break;
			}
			return rval;
		}
}
