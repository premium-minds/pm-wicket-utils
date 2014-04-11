package com.premiumminds.webapp.wicket.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Display feedback messages of a {@link FormComponent} with a Bootstrap Popover on the component.
 * <p>
 * <p>
 * Example:
 *	<pre>
 *	&lt;div class="control-group" wicket:id="feedback"&gt;
 *	  &lt;input wicket:id="input" type="text" /&gt;
 *  &lt;/div&gt;
 *	</pre>
 * 
 * <p>
 * Java code:
 * <pre>
 *   add(new BootstrapFeedbackPopover("feedback").add(new TextField&lt;String&gt;("input")));
 * </pre>
 * 
 * <p>
 * <strong>NOTE:</strong> this popover has a special html class 'feedback-popover' to create custom css.
 * <p>
 * @author acamilo
 */
public class BootstrapFeedbackPopover extends WebMarkupContainer implements IFeedback {
	
	private static final long serialVersionUID = -4902896529648117240L;
	
	private IFeedbackMessageFilter filter;
	
	private FeedbackMessagesModel model;
	
	private Map<Component, List<FeedbackMessage>> messages;
	
	/**
	 * Instantiates a new bootstrap feedback popover.
	 *
	 * @param id the wicket:id
	 */
	public BootstrapFeedbackPopover(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		filter = new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -7726392072697648969L;

			public boolean accept(FeedbackMessage msg) {
				for(Component component : visitChildren(FormComponent.class)){
					Component reporter = msg.getReporter(); 
					if(reporter != null && reporter.equals(component)) return true;
				}
				return false;
			}
		};

		model = new FeedbackMessagesModel(getPage(), filter);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		List<FeedbackMessage> msgs = model.getObject();
		messages = new HashMap<Component, List<FeedbackMessage>>();
		
		for(FeedbackMessage msg : msgs){
			if(!messages.containsKey(msg.getReporter())) messages.put(msg.getReporter(), new ArrayList<FeedbackMessage>());
			messages.get(msg.getReporter()).add(msg);
			msg.markRendered();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		List<FeedbackMessage> msgs = model.getObject();
		if(msgs.size()>0){
			for(Component component: messages.keySet()){
				StringBuffer sb = new StringBuffer();
				for(FeedbackMessage msg : messages.get(component)){
					sb.append(msg.getMessage()+"\n");
					msg.markRendered();
				}
				
				String script = "$(\"#"+component.getMarkupId()+"\")"
						+ ".popover({ 'trigger': 'focus', "
								   + "'placement': 'top', "
								   + "'content': \""+StringEscapeUtils.escapeEcmaScript(sb.toString())+"\", "
								   + "'template': '<div class=\"popover feedback-popover\"><div class=\"arrow\"></div><div class=\"popover-inner\"><h3 class=\"popover-title\"></h3><div class=\"popover-content\"><p></p></div></div></div>'"
						+ "});";
				script += "$(\"#"+component.getMarkupId()+"\").keypress(function(){ $(\"#"+this.getMarkupId()+"\").removeClass('has-error'); $(this).popover('destroy'); });";
				response.render(OnDomReadyHeaderItem.forScript(script));
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		
		if(model.getObject().size() > 0) tag.append("class", "has-error", " ");
	}
	
	/**
	 * Get the filter used by this feedback collector.
	 *
	 * @return the filter
	 */
	public IFeedbackMessageFilter getFilter(){
		return filter;
	}
	
}
