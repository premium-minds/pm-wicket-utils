package com.premiumminds.webapp.wicket;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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
