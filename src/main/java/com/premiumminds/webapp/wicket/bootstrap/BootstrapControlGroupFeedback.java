package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class BootstrapControlGroupFeedback extends WebMarkupContainer implements IFeedback {
	private static final long serialVersionUID = -4902896529648117240L;
	
	public BootstrapControlGroupFeedback(String id) {
		super(id);
		
		final FeedbackMessagesModel model = new FeedbackMessagesModel(this);
		model.setFilter(new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -7726392072697648969L;

			public boolean accept(FeedbackMessage msg) {
				for(Component component : visitChildren(FormComponent.class)){
					if(msg.getReporter().equals(component)) return true;
				}
				return false;
			}
		});
		
		add(AttributeModifier.append("class", new AbstractReadOnlyModel<String>(){
			private static final long serialVersionUID = -8590452835685880759L;

			@Override
			public String getObject() {
				return (model.getObject().size()>0) ? "error" : null;
			}
		}));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(OnDomReadyHeaderItem.forScript("$(\"#"+this.getMarkupId(true)+"\").focusin(function(){ $(this).removeClass(\"error\"); })"));
	}
	
}
