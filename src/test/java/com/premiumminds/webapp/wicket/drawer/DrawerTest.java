/**
 * Copyright (C) 2014 Premium Minds.
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

package com.premiumminds.webapp.wicket.drawer;

import static org.junit.Assert.*;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;

import com.premiumminds.webapp.wicket.AbstractComponentTest;

public class DrawerTest extends AbstractComponentTest {
	private class TestDrawer extends AbstractDrawer {
		private static final long serialVersionUID = 1L;

		private Panel header;
		private Panel content;
		private Panel footer;

		public Panel getCreatedHeader() {
			return header;
		}

		public Panel getCreatedContent() {
			return content;
		}

		public Panel getCreatedFooter() {
			return footer;
		}

		@Override
		protected Panel getHeader(String id) {
			header = super.getHeader(id);
			return header;
		}

		@Override
		protected Panel getContent(String id) {
			content = super.getContent(id);
			return content;
		}

		@Override
		protected Panel getFooter(String id) {
			footer = super.getFooter(id);
			return footer;
		}
	}

	@Test
	public void testDrawerCreation() {
		AbstractDrawer d = new TestDrawer();
		assertEquals("drawer", d.getId());
	}

	@Test
	public void testManagerManagement() {
		AbstractDrawer d = new TestDrawer();
		DrawerManager m = new DrawerManager("test");
		d.setManager(m);
		assertEquals(m, d.getManager());
	}

	@Test
	public void testAllowCloseManagement() {
		AbstractDrawer d = new TestDrawer();
		assertTrue(d.isAllowClose());
		d.setAllowClose(false);
		assertFalse(d.isAllowClose());
		d.setAllowClose(true);
		assertTrue(d.isAllowClose());
	}

	@Test
	public void testEmptyOnClose() {
		AbstractDrawer d = new TestDrawer();
		startTest(d);

		replayAll();

		d.beforeClose(getTarget());
		verifyAll();
	}

	@Test
	public void testInitialization() {
		TestDrawer d = new TestDrawer();
		startTest(d);

		replayAll();

		verifyAll();

		getTester().assertComponent(d.getPageRelativePath() + ":header", EmptyPanel.class);
		getTester().assertComponent(d.getPageRelativePath() + ":content", EmptyPanel.class);
		getTester().assertComponent(d.getPageRelativePath() + ":footer", EmptyPanel.class);
		assertEquals(d, d.getCreatedHeader().getParent());
		assertEquals(d, d.getCreatedContent().getParent());
		assertEquals(d, d.getCreatedFooter().getParent());
	}
}
