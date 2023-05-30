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
package com.premiumminds.webapp.wicket.repeaters;

import java.util.List;

import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AjaxListSetViewTest {
	private WicketTester tester;

	private String expectedFormat = 
	"<tr wicket:id=\"list\" id=\"%3$s\">\n" +
		"<td wicket:id=\"label\">%2$s</td>\n" +
		"<td><input type=\"text\" wicket:id=\"textfield\" value=\"%2$s\" name=\"list:%1$d:textfield\"/></td>\n" +
	"</tr>\n";
	
	@Test
	public void testRenderOk(){
		tester.startPage(new Page());
		tester.assertRenderedPage(Page.class);
		
		tester.assertLabel("list:1:label", "three");
		tester.assertModelValue("list:1:textfield", "three");
		tester.assertLabel("list:2:label", "five");
		tester.assertModelValue("list:2:textfield", "five");
		tester.assertLabel("list:3:label", "ten");
		tester.assertModelValue("list:3:textfield", "ten");
	}

	@Test
	public void testRenderInitialOrder(){
		tester.startPage(new Page());
		tester.assertRenderedPage(Page.class);

		List<TagTester> tags = tester.getTagsByWicketId("list");
		assertEquals(3, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 1, "three", "id12")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 2, "five", "id23")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "ten", "id34")));
	}

	@Test
	public void testInsertionOrder(){
		Page page = new Page();
		tester.startPage(page);
		tester.assertRenderedPage(Page.class);

		page.model.getObject().add("two");
		page.model.getObject().add("one");

		tester.startPage(page);
		tester.assertRenderedPage(Page.class);
		
		List<TagTester> tags = tester.getTagsByWicketId("list");
		assertEquals(5, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 1, "three", "id12")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 2, "five", "id23")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "ten", "id34")));
		assertThat(tags.get(3).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 4, "two", "id45")));
		assertThat(tags.get(4).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 5, "one", "id56")));
	}

	@Test
	public void testSwapElements(){
		Page page = new Page();
		tester.startPage(page);
		tester.assertRenderedPage(Page.class);

		page.model.getObject().remove("five");
		page.model.getObject().add(2, "five");

		tester.startPage(page);
		tester.assertRenderedPage(Page.class);
		
		List<TagTester> tags = tester.getTagsByWicketId("list");
		assertEquals(3, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 1, "three", "id12")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "ten", "id34")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 2, "five", "id23")));
	}
	
	public class Page extends WebPage {

		private static final long serialVersionUID = 2625516294774975788L;
		
		public AjaxListSetView<String> listView;
		public IModel<ListOrderedSet<String>> model;
		
		{
			ListOrderedSet<String> list = new ListOrderedSet<String>();
			list.add("three");
			list.add("five");
			list.add("ten");
			
			model = new Model<ListOrderedSet<String>>(list);

			listView = new AjaxListSetView<String>("list", model, "tbody") {
				private static final long serialVersionUID = -4677792487869300630L;

				@Override
				protected void populateItem(ListSetItem<String> item) {
					item.add(new Label("label", item.getModel()));
					item.add(new TextField<String>("textfield", item.getModel()));
				}
			};
			add(listView);
		}
	}
	
	@BeforeEach
	public void setUp(){
		tester = new WicketTester(new WebApplication() {
			
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}
			
		});
	}
}
