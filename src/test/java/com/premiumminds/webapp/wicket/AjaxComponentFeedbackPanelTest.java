package com.premiumminds.webapp.wicket;

import static org.junit.Assert.*;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.junit.Test;

public class AjaxComponentFeedbackPanelTest extends AbstractComponentTest {
	private static final String msg = "Message!";

	private class TestPanel extends AjaxComponentFeedbackPanel {
		private static final long serialVersionUID = 1L;

		private AjaxLink<String> link;
		private Label label;

		private FeedbackMessage message;

		public TestPanel(String id) {
			super(id);

			message = null;

			link = new AjaxLink<String>("link") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					Session.get().getFeedbackMessages().add(new FeedbackMessage(label, msg, 0));
					target.add(TestPanel.this);
				}
			};

			label = new Label("label");
		}

		public FeedbackMessage getMessage() {
			return message;
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			add(link);
			add(label);

			setMessageHolder(label);
		}

		@Override
		protected void onDisplayError(FeedbackMessage message) {
			label.setDefaultModel(new Model<String>((String)message.getMessage()));
			this.message = message;
		}
	}

	@Test
	public void testInitialization() {
		TestPanel p = new TestPanel("panel");
		startTest(p);
		getTester().assertInvisible(p.label.getPageRelativePath());
		assertTrue(p.getOutputMarkupId());
	}

	@Test
	public void testMessagesCaught() {
		TestPanel p = new TestPanel("panel");
		p.setFeedbackFor(p.label);
		startTest(p, false);

		getTester().clickLink(p.link);

		assertEquals(p.label, p.getMessage().getReporter());
		assertEquals(msg, p.getMessage().getMessage());
		assertTrue(p.getMessage().isRendered());
		getTester().assertLabel(p.label.getPageRelativePath(), "Message!");
	}
}
