package com.premiumminds.webapp.wicket.bootstrap;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class BootstrapFeedbackPanel extends Panel implements IFeedback {
	private static final long serialVersionUID = 918157933592698927L;

	private final ListView<FeedbackMessage> messageListView;
	
	public BootstrapFeedbackPanel(String id, final int level) {
		super(id);
		
		FeedbackMessagesModel model = new FeedbackMessagesModel(this);
		model.setFilter(new IFeedbackMessageFilter() {
			private static final long serialVersionUID = 3499027878665605756L;

			public boolean accept(FeedbackMessage m) {
				return m.getLevel()==level;
			}
		});
		
		messageListView = new ListView<FeedbackMessage>("message", model) {
			private static final long serialVersionUID = -3957832180171180089L;

			@Override
			protected IModel<FeedbackMessage> getListItemModel(
					final IModel<? extends List<FeedbackMessage>> listViewModel,
					final int index) {
				return new AbstractReadOnlyModel<FeedbackMessage>() {
					private static final long serialVersionUID = -3342035734198486676L;

					@Override
					public FeedbackMessage getObject() {
						if(index>= listViewModel.getObject().size()) return null;
						else return listViewModel.getObject().get(index);
					}
				};
			}
			
			@Override
			protected void populateItem(ListItem<FeedbackMessage> item) {
				Serializable serializable = item.getModel().getObject().getMessage();
				item.getModel().getObject().markRendered();
				Label label = new Label("msg", (serializable == null) ? "" : serializable.toString());
				label.setEscapeModelStrings(BootstrapFeedbackPanel.this.getEscapeModelStrings());
				item.add(label);
			}
		};
		add(messageListView);
	}
	
	public final boolean anyMessage()
	{
		List<FeedbackMessage> msgs = getCurrentMessages();
		return !msgs.isEmpty();
	}	
	
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		boolean anyMessage = anyMessage();
		setVisible(anyMessage);
	}
	
	/**
	 * Gets the currently collected messages for this panel.
	 * 
	 * @return the currently collected messages for this panel, possibly empty
	 */
	protected final List<FeedbackMessage> getCurrentMessages()
	{
		final List<FeedbackMessage> messages = messageListView.getModelObject();
		return Collections.unmodifiableList(messages);
	}	
}
