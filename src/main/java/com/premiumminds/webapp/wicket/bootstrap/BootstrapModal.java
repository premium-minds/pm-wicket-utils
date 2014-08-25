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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;


/**
 * A bootstrap Modal (<a href="http://getbootstrap.com/javascript/#modals">http://getbootstrap.com/2.3.2/javascript/#modals</a>)
 * <p>
 * <p>
 * Java Code:
 * <pre>
 *    public abstract class ConfirmModal extends BootstrapModal{
 *		public ConfirmModal(String id) {
 *			super(id);
 *
 *			AjaxLink<Void> confirmBtn = new AjaxLink<Void>("closeOk") {
 *				public void onClick(AjaxRequestTarget target) {
 *					hide();
 *					target.add(ConfirmModal.this);
 *				}
 *			};
 *			add(confirmBtn);
 *		}
 *	}
 * </pre>
 * <p>
 * <p>
 * HTML ConfirmModal.html:
 * <pre>
 * &lt;wicket:extend&gt;
 *			&lt;div class=&quot;modal-header&quot;&gt;		
 *				&lt;button type=&quot;button&quot; class=&quot;close&quot; data-dismiss=&quot;modal&quot; aria-hidden=&quot;true&quot;&gt;&amp;times;&lt;/button&gt;
 *				&lt;h4 class=&quot;modal-title&quot;&gt;Exit&lt;/h4&gt;
 *			&lt;/div&gt;
 *			&lt;div class=&quot;modal-body&quot;&gt;
 *				Do you really want to exit?
 *			&lt;/div&gt;
 *			&lt;div class=&quot;modal-footer&quot;&gt;
 *		        &lt;button type=&quot;button&quot; class=&quot;btn btn-default&quot; data-dismiss=&quot;modal&quot; aria-hidden=&quot;true&quot;&gt;Cancel&lt;/button&gt;
 *		        &lt;button type=&quot;button&quot; class=&quot;btn btn-primary&quot;&gt;Confirm&lt;/button&gt;
 *		    &lt;/div&gt;			
 *	&lt;/wicket:extend&gt;
 * </pre>
 */
public abstract class BootstrapModal extends Panel{
	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer container;

	/**
	 * Instantiates a new bootstrap modal, not visible. To show it see {@link #show()}.
	 *
	 * @param id the wicket:id
	 */
	public BootstrapModal(String id) {
		super(id);
		container = new WebMarkupContainer("container");
		container.setVisible(false);
		container.setOutputMarkupPlaceholderTag(true);
		
		add(new AjaxEventBehavior("hidden.bs.modal") {
			private static final long serialVersionUID = 1096860362709362617L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				container.setVisible(false);
				target.add(container);
			}
		});
		add(new AjaxEventBehavior("shown.bs.modal") {
			private static final long serialVersionUID = 6569358950560898308L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				container.setVisible(true);
				target.add(container);
			}
		});

		super.add(container);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		StringBuilder sb = new StringBuilder();
		if(container.isVisible()) sb.append("$('#"+BootstrapModal.this.getMarkupId()+"').modal('show');");
		else sb.append("$('#"+BootstrapModal.this.getMarkupId()+"').modal('hide');");
		response.render(OnDomReadyHeaderItem.forScript(sb.toString()));
	}

	/**
	 * Show the modal. If the request is AJAX, you need to add the modal to the {@link AjaxRequestTarget}
	 */
	public void show(){
		container.setVisible(true);
	}
	
	/**
	 * Hide the modal. If the request is AJAX, use {@link #hide(AjaxRequestTarget)}
	 */
	public void hide(){
		container.setVisible(false);
	}
	
	/**
	 * Hide the modal. Just sends the javascript to close the modal
	 *
	 * @param target the AjaxRequestTarget
	 */
	public void hide(AjaxRequestTarget target){
		target.prependJavaScript("$('#"+getMarkupId()+"').modal('hide');");
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#add(org.apache.wicket.Component[])
	 */
	@Override
	public MarkupContainer add(Component... components) {
		return container.add(components);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#addOrReplace(org.apache.wicket.Component[])
	 */
	@Override
	public MarkupContainer addOrReplace(Component... components) {
		return container.addOrReplace(components);
	}
}
