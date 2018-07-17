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

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.io.IClusterable;

public class ConfirmationDialogModal extends ModalWindow {
	private static final long serialVersionUID = 3690754099561035011L;

	public enum Result { OK, CANCELED };
	
	private EventHandler eventHandler;
	
	@SuppressWarnings("serial")
	public ConfirmationDialogModal(String id) {
		super(id);
		setResizable(false);
		setHeightUnit("px");
		setWidthUnit("px");
		setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			public void onClose(AjaxRequestTarget target) {
				confirmed(target, Result.CANCELED);
			}
		});
	}
	
	private void confirmed(AjaxRequestTarget target, Result result){
		if(eventHandler!=null) eventHandler.onConfirm(target, result);
	}
	
	public void show(String title, String message, String verb, AjaxRequestTarget target, EventHandler eventHandler) {
		setContent(new ConfirmPanel(getContentId(), message, verb));
		setTitle(title);
		
		this.eventHandler = eventHandler;
		
		show(target);
	}
	
	@SuppressWarnings("serial")
	private class ConfirmPanel extends Panel {

		public ConfirmPanel(String id, String message, String verb) {
			super(id);
			
			add(new Label("message", message));
			add(new AjaxFallbackLink<Void>("noButton"){
				@Override
				public void onClick(Optional<AjaxRequestTarget> target) {
					if (target.isPresent()) {
						ConfirmationDialogModal.this.close(target.get());
						confirmed(target.get(), Result.CANCELED);
					}
				}
			}.add(new Label("noLbl", "Cancelar")));
			WebMarkupContainer yesButton = new AjaxFallbackLink<Void>("yesButton"){
				@Override
				public void onClick(Optional<AjaxRequestTarget>  target) {
					if (target.isPresent()) {
						ConfirmationDialogModal.this.close(target.get());
						confirmed(target.get(), Result.OK);
					}
				}
			}; 
			yesButton.add(new Label("okLbl", verb));
		
			add(yesButton);
		}
	}
	
	public static interface EventHandler extends IClusterable {
		public void onConfirm(AjaxRequestTarget target, Result result);
	}
}
