package com.premiumminds.webapp.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.io.IClusterable;

public class ConfirmationDialogModal extends ModalWindow{
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
				public void onClick(AjaxRequestTarget target) {
					ConfirmationDialogModal.this.close(target);
					confirmed(target, Result.CANCELED);
				}
			}.add(new Label("noLbl", "Cancelar")));
			WebMarkupContainer yesButton = new AjaxFallbackLink<Void>("yesButton"){
				@Override
				public void onClick(AjaxRequestTarget target) {
					ConfirmationDialogModal.this.close(target);
					confirmed(target, Result.OK);
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
