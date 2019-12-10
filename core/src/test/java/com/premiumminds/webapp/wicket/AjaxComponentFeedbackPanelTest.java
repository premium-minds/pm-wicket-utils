/**
 * Copyright (C) 2016 Premium Minds.
 *
 * This file is part of pm-wicket-utils.
 *
 * pm-wicket-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-wicket-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-wicket-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.wicket;

import static org.junit.Assert.*;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.junit.Test;

import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

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
