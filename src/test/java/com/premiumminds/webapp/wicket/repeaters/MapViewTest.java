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
package com.premiumminds.webapp.wicket.repeaters;

import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

public class MapViewTest {
	private WicketTester tester;

	private String expectedFormat = 
	"<tr wicket:id=\"map\">\n" +
		"<td wicket:id=\"label\">%2$s</td>\n" +
		"<td><input type=\"text\" wicket:id=\"textfield\" value=\"%2$s\" name=\"map:%1$d:textfield\"/></td>\n" +
	"</tr>\n";
	
	@Test
	public void testRenderOk(){
		tester.startPage(new Page());
		tester.assertRenderedPage(Page.class);
		
		tester.assertLabel("map:3:label", "three");
		tester.assertModelValue("map:3:textfield", "three");
		tester.assertLabel("map:5:label", "five");
		tester.assertModelValue("map:5:textfield", "five");
		tester.assertLabel("map:10:label", "ten");
		tester.assertModelValue("map:10:textfield", "ten");
	}

	@Test
	public void testRenderInitialOrder(){
		tester.startPage(new Page());
		tester.assertRenderedPage(Page.class);

		List<TagTester> tags = tester.getTagsByWicketId("map");
		assertEquals(3, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "three")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 5, "five")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 10, "ten")));
	}

	@Test
	public void testInsertedOrderWithoutInversion(){
		Page page = new Page();
		tester.startPage(page);
		tester.assertRenderedPage(Page.class);

		page.mapView.getModelObject().put(2, "two");
		page.mapView.getModelObject().put(1, "one");

		tester.startPage(page);
		tester.assertRenderedPage(Page.class);
		
		List<TagTester> tags = tester.getTagsByWicketId("map");
		assertEquals(5, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "three")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 5, "five")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 10, "ten")));
		assertThat(tags.get(3).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 1, "one")));
		assertThat(tags.get(4).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 2, "two")));
	}

	@Test
	public void testInsertedOrderWithInversion(){
		Page page = new Page();
		page.mapView.setInsertionOrderInverted(true);
		tester.startPage(page);
		tester.assertRenderedPage(Page.class);

		page.mapView.getModelObject().put(2, "two");
		page.mapView.getModelObject().put(1, "one");

		tester.startPage(page);
		tester.assertRenderedPage(Page.class);
		
		List<TagTester> tags = tester.getTagsByWicketId("map");
		assertEquals(5, tags.size());
		
		assertThat(tags.get(0).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 2, "two")));
		assertThat(tags.get(1).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 1, "one")));
		assertThat(tags.get(2).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 3, "three")));
		assertThat(tags.get(3).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 5, "five")));
		assertThat(tags.get(4).getMarkup(), equalToIgnoringWhiteSpace(String.format(expectedFormat, 10, "ten")));
	}
	
	public class Page extends WebPage {

		private static final long serialVersionUID = 2625516294774975788L;
		
		public MapView<Integer, String> mapView;
		
		{
			Map<Integer, String> map = new HashMap<Integer, String>();
			map.put(3, "three");
			map.put(5, "five");
			map.put(10, "ten");

			mapView = new MapView<Integer, String>("map", Model.ofMap(map)) {
				private static final long serialVersionUID = -4677792487869300630L;

				@Override
				protected void populateItem(MapItem<Integer, String> item) {
					item.add(new Label("label", item.getModel()));
					item.add(new TextField<String>("textfield", item.getModel()));
				}
			};
			add(mapView);
		}
	}
	
	@Before
	public void setUp(){
		tester = new WicketTester(new WebApplication() {
			
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}
			
		});
	}
}
