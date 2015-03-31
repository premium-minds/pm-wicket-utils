package com.premiumminds.webapp.wicket;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.IDetachable;
import org.easymock.EasyMockSupport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PageableListModelTest extends EasyMockSupport {
	public final List<Integer> theList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	
	private class TestModel extends PageableListModel<List<Integer>> {
		private static final long serialVersionUID = 1L;

		public List<Integer> list = theList;
		public int count = 0;

		public TestModel() {
			super(10);
		}

		@Override
		public List<Integer> getPageList(int startIndex, int viewSize) {
			count++;
			return list;
		}
	}

	private interface DetachableInterface extends List<Integer>, IDetachable {}
	private interface NonDetachableInterface extends List<Integer> {}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testCreationAndGetObject() {
		TestModel model = new TestModel();

		assertEquals(10, model.getSize());

		assertEquals(0, model.count);
		assertEquals(theList, model.getObject());
		assertEquals(1, model.count);
		assertEquals(theList, model.getObject());
		assertEquals(1, model.count);
	}

	@Test
	public void testSetObjectUnsupported() {
		exception.expect(UnsupportedOperationException.class);

		TestModel model = new TestModel();
		model.setObject(theList);
	}

	@Test
	public void testStartIndex() {
		TestModel model = new TestModel();

		model.setStartIndex(5, 3);

		assertEquals(5, model.getStartIndex());
		assertEquals(3, model.getViewSize());
		assertEquals(1, model.count);

		model.setStartIndex(5, 3);

		assertEquals(1, model.count);

		model.setStartIndex(5, 5);

		assertEquals(5, model.getStartIndex());
		assertEquals(5, model.getViewSize());
		assertEquals(2, model.count);

		model.setStartIndex(10, 5);

		assertEquals(10, model.getStartIndex());
		assertEquals(5, model.getViewSize());
		assertEquals(3, model.count);
	}

	@Test
	public void testEqualsAndHashCode() {
		TestModel m1 = new TestModel();
		TestModel m2 = new TestModel();

		assertTrue(m1.equals(m1));
		assertFalse(m1.equals(null));
		assertFalse(m1.equals(new PageableListModel<List<Integer>>(10) {
			private static final long serialVersionUID = 1L;
			@Override
			public List<Integer> getPageList(int startIndex, int viewSize) {
				return null;
			}}));

		assertTrue(m1.equals(m2));
		assertTrue(m1.hashCode() == m2.hashCode());

		m2.getObject();

		assertFalse(m1.equals(m2));
		assertFalse(m1.hashCode() == m2.hashCode());

		m1.getObject();

		assertTrue(m1.equals(m2));
		assertTrue(m1.hashCode() == m2.hashCode());

		m1.setStartIndex(5, 3);

		assertFalse(m1.equals(m2));
		assertFalse(m1.hashCode() == m2.hashCode());

		m2.setStartIndex(5, 3);

		assertTrue(m1.equals(m2));
		assertTrue(m1.hashCode() == m2.hashCode());

		m2.setStartIndex(5, 5);

		assertFalse(m1.equals(m2));
		assertFalse(m1.hashCode() == m2.hashCode());

		m2.list = Arrays.asList(1, 2, 3);
		m2.setStartIndex(5, 3);

		assertFalse(m1.equals(m2));
		assertFalse(m1.hashCode() == m2.hashCode());
	}

	@Test
	public void testDetach() {
		TestModel model = new TestModel();
		model.list = createMock(DetachableInterface.class);
		((IDetachable)model.list).detach();

		replayAll();

		model.getObject();
		model.detach();

		verifyAll();
	}

	@Test
	public void testDetachNonDetachable() {
		TestModel model = new TestModel();
		model.list = createMock(NonDetachableInterface.class);

		replayAll();

		model.getObject();
		model.detach();

		verifyAll();
	}
}
