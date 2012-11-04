package com.premiumminds.webapp.wicket.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;

public class BootstrapControlGroupFeedback extends WebMarkupContainer implements IFeedback {
	private static final long serialVersionUID = -4902896529648117240L;
	
	private IFeedbackMessageFilter filter;
	
	public BootstrapControlGroupFeedback(String id) {
		super(id);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		filter = new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -7726392072697648969L;

			public boolean accept(FeedbackMessage msg) {
				for(Component component : visitChildren(FormComponent.class)){
					if(msg.getReporter().equals(component)) return true;
				}
				return false;
			}
		};
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(OnDomReadyHeaderItem.forScript("$(\"#"+this.getMarkupId(true)+"\").focusin(function(){ $(this).removeClass(\"error\"); })"));
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if(new FeedbackCollector(getPage()).collect(filter).size() > 0) tag.append("class", "error", " ");
	}
	
}
