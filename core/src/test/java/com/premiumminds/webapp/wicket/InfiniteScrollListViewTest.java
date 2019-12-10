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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupFragment;
import org.apache.wicket.markup.html.list.ListItem;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.premiumminds.webapp.wicket.testing.AbstractComponentTest;

public class InfiniteScrollListViewTest extends AbstractComponentTest {
	private abstract class TestList extends PageableListModel<List<Integer>> {
		private static final long serialVersionUID = 1L;

		public TestList() {
			super(10);
		}
	}

	private class TestView extends InfiniteScrollListView<Integer> {
		private static final long serialVersionUID = 1L;

		public TestView(String id, PageableListModel<? extends List<? extends Integer>> model) {
			super(id, model);
		}

		@Override
		public IMarkupFragment getMarkup() {
			IMarkupFragment all = getAssociatedMarkup();
			for (int i = 0; i < all.size(); i++ ) {
				if (!all.get(i).toString().startsWith("<!--"))
					return new MarkupFragment(all,  i);
			}

			return all;
		}

		@Override
		protected void populateItem(ListItem<Integer> item) {
			return;
		}

		@Override
		protected String getItemMarkupId(Integer item) {
			return this.getMarkupId() + item;
		}
	}

	private final List<Integer> theList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

	private TestList list;

	@Before
	public void createMock() {
		list = createMock(TestList.class);
		expect(list.getSize()).andReturn(10).anyTimes();
		expect(list.getStartIndex()).andReturn(0).anyTimes();
		expect(list.getObject()).andReturn(theList).anyTimes();
		list.setStartIndex(0, 100);
		expectLastCall().anyTimes();
		list.detach();
		expectLastCall().anyTimes();
	}

	@Test
	public void testInitialization() {
		EasyMock.replay(list);

		TestView v = new TestView("view", list);
		startTest(v);

		EasyMock.replay(getTarget());

		verifyAll();

		getTester().assertComponent(v.getPageRelativePath(), TestView.class);
		//TODO: Assert component structure
	}
}
