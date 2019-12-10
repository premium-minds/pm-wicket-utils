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
package com.premiumminds.webapp.wicket.drawer;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

public class DrawerManagerTest extends AbstractComponentTest {
	//Some tests are highly brittle, as they treat AJAX communication details (which should be
	//considered part the of implementation) as if they were part of the component interface.
	//Setting up a proper interface unit test would entail instantiating a JavaScript engine.
	//This is the next best thing. - JMMM

	private class TestDrawer extends AbstractDrawer {
		private static final long serialVersionUID = 1L;

		private boolean mbOnCloseCalled = false;

		public boolean getOnCloseCalled() {
			return mbOnCloseCalled;
		}

		@Override
		public void beforeClose(AjaxRequestTarget target) {
			super.beforeClose(target);

			mbOnCloseCalled = true;
		}
	}

	public DrawerManagerTest() {}

	@Before
	public void setup() {}

	@After
	public void teardown() {}

	@Test
	public void testManagerCreation() {
		DrawerManager m = new DrawerManager("test");
		startTest(m);

		replayAll();

		verifyAll();

		assertEquals("test", m.getId());
		getTester().assertComponent(m.getPageRelativePath(), DrawerManager.class);
	}

	@Test
	public void testEmptyManager() {
		DrawerManager m = new DrawerManager("test");
		startTest(m);

		replayAll();

		verifyAll();

		assertNull(m.getLast(AbstractDrawer.class));
		assertNull(m.getLastItemRelativePath());
	}

	@Test
	public void testPushSingleDrawerNoAJAX() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d = new TestDrawer();
		m.push(d);
		startTest(m);

		replayAll();

		verifyAll();

		assertEquals(d, m.getLast(AbstractDrawer.class));
		assertEquals(d.getParent().getPageRelativePath(), m.getLastItemRelativePath());
		getTester().assertComponent(d.getPageRelativePath(), TestDrawer.class);
	}

	@Test
	public void testPushSingleDrawerWithCSS() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d = new TestDrawer();
		m.push(d, "some-css-class");
		startTest(m);

		replayAll();

		verifyAll();

		assertEquals(d, m.getLast(AbstractDrawer.class));
		assertThat(getTester().getTagByWicketId(d.getParent().getId()).getAttribute("class"), CoreMatchers.endsWith("some-css-class"));
	}

	@Test
	public void testPushMultipleDrawersWithAJAX() {
		Capture<Panel> item1 = EasyMock.newCapture();
		Capture<Panel> item2 = EasyMock.newCapture();
		Capture<Panel> item3 = EasyMock.newCapture();
		Capture<String> str1 = EasyMock.newCapture();
		Capture<String> str2 = EasyMock.newCapture();
		Capture<String> str2a = EasyMock.newCapture();
		Capture<String> str2b = EasyMock.newCapture();
		Capture<String> str3 = EasyMock.newCapture();
		Capture<String> str3a = EasyMock.newCapture();
		Capture<String> str3b = EasyMock.newCapture();
		DrawerManager m = new DrawerManager("test");
		startTest(m);

		getTarget().add(capture(item1));
		getTarget().appendJavaScript(capture(str1));
		getTarget().add(capture(item2));
		getTarget().appendJavaScript(capture(str2));
		getTarget().appendJavaScript(capture(str2a));
		getTarget().appendJavaScript(capture(str2b));
		getTarget().add(capture(item3));
		getTarget().appendJavaScript(capture(str3));
		getTarget().appendJavaScript(capture(str3a));
		getTarget().appendJavaScript(capture(str3b));
		replayAll();

		AbstractDrawer d1 = new TestDrawer();
		m.push(d1, getTarget());
		AbstractDrawer d2 = new TestDrawer();
		m.push(d2, getTarget());
		AbstractDrawer d3 = new TestDrawer();
		m.push(d3, getTarget());
		assertEquals(d3, m.getLast(AbstractDrawer.class));
		getTester().assertComponent(d3.getPageRelativePath(), TestDrawer.class);
		verifyAll();

		assertEquals(d1.getParent().getParent(), item1.getValue());
		assertEquals(d2.getParent().getParent(), item2.getValue());
		assertEquals(d3.getParent().getParent(), item3.getValue());
		assertEquals("$('#"+d1.getParent().getMarkupId()+"').modaldrawer('show');", str1.getValue());
		assertEquals("$('#"+d2.getParent().getMarkupId()+"').modaldrawer('show');", str2.getValue());
		assertEquals("$('#"+d1.getParent().getMarkupId()+"').removeClass('shown-modal');", str2a.getValue());
		assertEquals("$('#"+d1.getParent().getMarkupId()+"').addClass('hidden-modal');", str2b.getValue());
		assertEquals("$('#"+d3.getParent().getMarkupId()+"').modaldrawer('show');", str3.getValue());
		assertEquals("$('#"+d2.getParent().getMarkupId()+"').removeClass('shown-modal');", str3a.getValue());
		assertEquals("$('#"+d2.getParent().getMarkupId()+"').addClass('hidden-modal');", str3b.getValue());
	}

	@Test
	public void testPopFromEmptyManager() {
		DrawerManager m = new DrawerManager("test");
		startTest(m);

		replayAll();

		AbstractDrawer d = new TestDrawer();
		m.pop(d, getTarget());
		verifyAll();

		assertNull(m.getLast(AbstractDrawer.class));
	}

	@Test
	public void testPopSingleDrawer() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d = new TestDrawer();
		m.push(d);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d.getParent().getMarkupId()+"').modaldrawer('hide');");
		replayAll();

		m.pop(d, getTarget());
		verifyAll();
	}

	@Test
	public void testPopMultipleDrawers() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d1 = new TestDrawer();
		m.push(d1);
		AbstractDrawer d2 = new TestDrawer();
		m.push(d2);
		AbstractDrawer d3 = new TestDrawer();
		m.push(d3);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').modaldrawer('hide');");
		replayAll();

		m.pop(d2, getTarget());
		verifyAll();
	}

	@Test
	public void testPopUnpushedDrawer() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d1 = new TestDrawer();
		m.push(d1);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d1.getParent().getMarkupId()+"').modaldrawer('hide');");
		replayAll();

		AbstractDrawer d2 = new TestDrawer();
		m.pop(d2, getTarget());
		verifyAll();
	}

	@Test
	public void testReplaceDrawerInEmptyManager() {
		DrawerManager m = new DrawerManager("test");
		startTest(m);

		replayAll();

		AbstractDrawer d = new TestDrawer();
		m.replaceLast(d, getTarget());
		verifyAll();
	}

	@Test
	public void testReplaceDrawer() {
		DrawerManager m = new DrawerManager("test");
		AbstractDrawer d1 = new TestDrawer();
		m.push(d1);
		startTest(m);

		AbstractDrawer d2 = new TestDrawer();
		getTarget().add(d2);
		replayAll();

		m.replaceLast(d2, getTarget());
		verifyAll();
	}

	@Test
	public void testCloseDrawerEvent() {
		Capture<EmptyPanel> p = EasyMock.newCapture();
		DrawerManager m = new DrawerManager("test");
		TestDrawer d = new TestDrawer();
		m.push(d);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d.getParent().getMarkupId()+"').unbind('hide-modal');");
		getTarget().appendJavaScript("$('#"+d.getParent().getMarkupId()+"').data('modal-drawer').isShown=true;");
		getTarget().appendJavaScript("$('#"+d.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d.getParent().getMarkupId()+"').removeClass('shown-modal');");
		getTarget().add(capture(p));
		replayAll();

		getTester().executeAjaxEvent(d.getParent(), "hide-modal");
		verifyAll();

		assertNull(m.getLast(AbstractDrawer.class));
		assertNull(m.getLastItemRelativePath());
		assertEquals(m, p.getValue().getParent());
		assertTrue(d.getOnCloseCalled());
	}

	@Test
	public void testCloseInnerDrawerEvent() {
		Capture<EmptyPanel> p = EasyMock.newCapture();
		DrawerManager m = new DrawerManager("test");
		TestDrawer d1 = new TestDrawer();
		m.push(d1);
		TestDrawer d2 = new TestDrawer();
		m.push(d2);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').unbind('hide-modal');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').data('modal-drawer').isShown=true;");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').removeClass('shown-modal');");
		getTarget().add(capture(p));
		getTarget().appendJavaScript("$('#"+d1.getParent().getMarkupId()+"').addClass('shown-modal');");
		getTarget().appendJavaScript("$('#"+d1.getParent().getMarkupId()+"').removeClass('hidden-modal');");
		replayAll();

		getTester().executeAjaxEvent(d2.getParent(), "hide-modal");
		verifyAll();

		assertEquals(d1, m.getLast(AbstractDrawer.class));
		assertEquals(d1.getParent().getParent(), p.getValue().getParent());
		assertFalse(d1.getOnCloseCalled());
		assertTrue(d2.getOnCloseCalled());
	}

	@Test
	public void testLockedDrawerDoesNotClose() {
		DrawerManager m = new DrawerManager("test");
		TestDrawer d = new TestDrawer();
		d.setAllowClose(false);
		m.push(d);
		startTest(m);

		replayAll();

		getTester().executeAjaxEvent(d.getParent(), "hide-modal");
		verifyAll();

		assertEquals(d, m.getLast(AbstractDrawer.class));
		assertTrue(d.getOnCloseCalled());
	}

	@Test
	public void testCloseDrawerEventForMultipleDrawers() {
		Capture<EmptyPanel> p2 = EasyMock.newCapture();
		Capture<EmptyPanel> p3 = EasyMock.newCapture();
		DrawerManager m = new DrawerManager("test");
		TestDrawer d1 = new TestDrawer();
		m.push(d1);
		TestDrawer d2 = new TestDrawer();
		m.push(d2);
		TestDrawer d3 = new TestDrawer();
		m.push(d3);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').unbind('hide-modal');");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').data('modal-drawer').isShown=true;");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').removeClass('shown-modal');");
		getTarget().add(capture(p3));
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').unbind('hide-modal');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').data('modal-drawer').isShown=true;");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').removeClass('shown-modal');");
		getTarget().add(capture(p2));
		getTarget().appendJavaScript("$('#"+d1.getParent().getMarkupId()+"').addClass('shown-modal');");
		getTarget().appendJavaScript("$('#"+d1.getParent().getMarkupId()+"').removeClass('hidden-modal');");
		replayAll();

		getTester().executeAjaxEvent(d2.getParent(), "hide-modal");
		verifyAll();

		assertEquals(d1, m.getLast(AbstractDrawer.class));
		assertEquals(d2.getParent().getParent(), p3.getValue().getParent());
		assertEquals(d1.getParent().getParent(), p2.getValue().getParent());
		assertTrue(d3.getOnCloseCalled());
		assertTrue(d2.getOnCloseCalled());
		assertFalse(d1.getOnCloseCalled());
	}

	@Test
	public void testLockedDrawerInHierarchyPreventsCloseOfLowerDrawers() {
		Capture<EmptyPanel> p = EasyMock.newCapture();
		DrawerManager m = new DrawerManager("test");
		TestDrawer d1 = new TestDrawer();
		m.push(d1);
		TestDrawer d2 = new TestDrawer();
		d2.setAllowClose(false);
		m.push(d2);
		TestDrawer d3 = new TestDrawer();
		m.push(d3);
		startTest(m);

		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').unbind('hide-modal');");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').data('modal-drawer').isShown=true;");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').modaldrawer('hide');");
		getTarget().appendJavaScript("$('#"+d3.getParent().getMarkupId()+"').removeClass('shown-modal');");
		getTarget().add(capture(p));
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').addClass('shown-modal');");
		getTarget().appendJavaScript("$('#"+d2.getParent().getMarkupId()+"').removeClass('hidden-modal');");
		replayAll();

		getTester().executeAjaxEvent(d1.getParent(), "hide-modal");
		verifyAll();

		assertEquals(d2, m.getLast(AbstractDrawer.class));
		assertEquals(d2.getParent().getParent(), p.getValue().getParent());
		assertTrue(d3.getOnCloseCalled());
		assertTrue(d2.getOnCloseCalled());
		assertFalse(d1.getOnCloseCalled());
	}
}
