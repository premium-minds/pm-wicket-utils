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
package com.premiumminds.webapp.wicket.bootstrap;

import static org.junit.Assert.*;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import com.premiumminds.webapp.wicket.bootstrap.BootstrapPaginator;

public class BootstrapPaginatorTest {
	@Test
	public void testRenderComponent(){
		WicketTester tester = createTester();
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
			}
		};
		paginator.setTotalResults(Model.of(100));
		tester.startComponentInPage(paginator);
		tester.assertDisabled("paginator:first:link");
		tester.assertDisabled("paginator:previous:link");
		tester.assertEnabled("paginator:next:link");
		tester.assertEnabled("paginator:last:link");
	}

	@Test
	public void testLastButton(){
		WicketTester tester = createTester();
		final Boxed<Integer> pageBox = new Boxed<Integer>();
		
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
				pageBox.value=page.getObject();
			}
		};
		paginator.setTotalResults(Model.of(100));
		paginator.setNumberResultsPerPage(10);
		tester.startComponentInPage(paginator);
		
		tester.clickLink("paginator:last:link");
		assertEquals(9, (int) pageBox.value);
		tester.assertDisabled("paginator:last:link");
		tester.assertDisabled("paginator:next:link");
		tester.assertEnabled("paginator:previous:link");
		tester.assertEnabled("paginator:first:link");
		
	}

	@Test
	public void testNextButton(){
		WicketTester tester = createTester();
		final Boxed<Integer> pageBox = new Boxed<Integer>();
		
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
				pageBox.value=page.getObject();
			}
		};
		paginator.setTotalResults(Model.of(100));
		tester.startComponentInPage(paginator);
		tester.clickLink("paginator:next:link");
		assertEquals(1, (int) pageBox.value);
		tester.assertEnabled("paginator:last:link");
		tester.assertEnabled("paginator:next:link");
		tester.assertEnabled("paginator:previous:link");
		tester.assertEnabled("paginator:first:link");
	}
	
	@Test
	public void testPreviousButton(){
		WicketTester tester = createTester();
		final Boxed<Integer> pageBox = new Boxed<Integer>();
		
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
				pageBox.value=page.getObject();
			}
		};
		paginator.setTotalResults(Model.of(100));
		paginator.setModelObject(2);
		tester.startComponentInPage(paginator);
		tester.clickLink("paginator:previous:link");
		assertEquals(1, (int) pageBox.value);
		tester.assertEnabled("paginator:last:link");
		tester.assertEnabled("paginator:next:link");
		tester.assertEnabled("paginator:previous:link");
		tester.assertEnabled("paginator:first:link");
	}

	@Test
	public void testFirstButton(){
		WicketTester tester = createTester();
		final Boxed<Integer> pageBox = new Boxed<Integer>();
		
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
				pageBox.value=page.getObject();
			}
		};
		paginator.setTotalResults(Model.of(100));
		paginator.setModelObject(2);
		tester.startComponentInPage(paginator);
		tester.clickLink("paginator:first:link");
		assertEquals(0, (int) pageBox.value);
		tester.assertEnabled("paginator:last:link");
		tester.assertEnabled("paginator:next:link");
		tester.assertDisabled("paginator:previous:link");
		tester.assertDisabled("paginator:first:link");
	}
	
	@Test
	public void testNumberButton(){
		WicketTester tester = createTester();
		final Boxed<Integer> pageBox = new Boxed<Integer>();
		
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;

			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
				pageBox.value=page.getObject();
			}
		};
		paginator.setTotalResults(Model.of(100));
		paginator.setNumberResultsPerPage(10);
		tester.startComponentInPage(paginator);
		tester.clickLink("paginator:page:4:link");
		assertEquals(4, (int) pageBox.value);
		tester.assertEnabled("paginator:last:link");
		tester.assertEnabled("paginator:next:link");
		tester.assertEnabled("paginator:previous:link");
		tester.assertEnabled("paginator:first:link");
		tester.assertLabel("paginator:page:2:link:label", "5");
	}
	
	@Test
	public void testShowHiddenComponents(){
		WicketTester tester = createTester();
		BootstrapPaginator paginator = new BootstrapPaginator("paginator") {
			private static final long serialVersionUID = -4486050808642574868L;
			
			@Override
			public void onPageChange(AjaxRequestTarget target, IModel<Integer> page) {
			}
		};
		
		paginator.setTotalResults(Model.of(100));
		paginator.setShowLastButton(false);
		paginator.setShowFirstButton(false);
		paginator.setShowNextButton(false);
		paginator.setShowPreviousButton(false);
		
		tester.startComponentInPage(paginator);
		tester.assertInvisible("paginator:first:link");
		tester.assertInvisible("paginator:previous:link");
		tester.assertInvisible("paginator:next:link");
		tester.assertInvisible("paginator:last:link");
	}
	
	private WicketTester createTester(){
		WicketTester tester = new WicketTester(new WebApplication() {
			
			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}
		}){
			@Override
			protected String createPageMarkup(String componentId) {
				return "<div class=\"pagination pagination-small pagination-right\" wicket:id=\"paginator\">"+
						"</div>";
			}
		};
		
		return tester;
	}
	
	private static class Boxed<T> {
		public T value;
	}
}