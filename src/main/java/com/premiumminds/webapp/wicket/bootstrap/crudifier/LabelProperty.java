package com.premiumminds.webapp.wicket.bootstrap.crudifier;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public abstract class LabelProperty extends Label {
	private static final long serialVersionUID = 4465124169249526543L;

	private IModel<?> model;
	private Map<Class<?>, IObjectRenderer<?>> renderers;

	public LabelProperty(String id, final IModel<?> model, Map<Class<?>, IObjectRenderer<?>> renderers) {
		super(id);
		this.model = model;
		this.renderers = renderers;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		setDefaultModel(new IModel<Object>(){
			private static final long serialVersionUID = -4951888022432211076L;

			public void detach() {
				model.detach();
			}

			public Object getObject() {
				Object obj = model.getObject();
				if(obj==null) return null;
				return convertObject(obj);
				
			}
			
			@SuppressWarnings("unchecked")
			private String convertObject(Object obj){
				if(obj.getClass().isEnum()) return getResourceString(obj.toString(), obj.toString());
				if(obj instanceof Boolean) return getResourceString(obj.toString(), obj.toString());
				if(obj instanceof Number) return MessageFormat.format(getResourceString("format", "{0,number,#.##}"), obj);
				if(obj instanceof Date) return MessageFormat.format(getResourceString("format", "{0,date,dd/MM/yyyy}"), obj);
				if(renderers.containsKey(obj.getClass())){
					return ((IObjectRenderer<Object>) renderers.get(obj.getClass())).render(obj);
				}
				if(obj instanceof Collection<?>){
					Collection<Object> collection = (Collection<Object>) obj;
					List<String> values = new ArrayList<String>();
					for(Object objCol : collection){ values.add(convertObject(objCol)); }
					return StringUtils.join(values, ", ");
				}
				return obj.toString();
			}

			public void setObject(Object object) {
				throw new RuntimeException("illegal operation");
			}
			
		});
	}

	protected abstract String getResourceString(String key, String defaultValue);
}
