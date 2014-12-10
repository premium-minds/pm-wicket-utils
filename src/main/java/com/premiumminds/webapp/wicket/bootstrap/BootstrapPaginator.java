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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

/**
 * 
 * @author acamilo
 * 
 * @version 1.4.1 - properties added to change the string (first, next, previous, last and elipsis)
 */
@SuppressWarnings("serial")
public abstract class BootstrapPaginator extends Panel implements IGenericComponent<Integer> {
	private static final long serialVersionUID = -5991811031611368885L;
	
	/*booleans to control if the component should be showned*/
	private boolean showFirstButton = true;
	private boolean showLastButton = true;
	private boolean showNextButton = true;
	private boolean showPreviousButton = true;
	private boolean showMorePagesInformation = false;
	
	/*booleans to control if the component should be hidden in specific cases*/
	private boolean hiddenFirstButton = false;
	private boolean hiddenLastButton = false;
	private boolean hiddenPreviousButton = false;
	private boolean hiddenNextButton = false;
	
	
	private int pagesToShow = 5;
	private IModel<Integer> pageNumberModel;
	private IModel<Integer> totalResults;
	private int numberResultsPerPage = 10;
	
	private int threshold=0;

	public BootstrapPaginator(String id) {
		super(id);
		setOutputMarkupId(true);
		
		pagesToShow = Math.max(3, pagesToShow);
		pageNumberModel = new Model<Integer>(0);
		totalResults = new Model<Integer>(100); // This recreates the original default of ten pages - JMMM

		add(new SquaresContainer("first"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(pageNumberModel.getObject()>0);
				setVisible(showFirstButton && (!hiddenFirstButton || threshold>0));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setPage(0);
				target.add(BootstrapPaginator.this);
				onPageChange(target, pageNumberModel);
			}
			
		}));
		add(new SquaresContainer("previous"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(pageNumberModel.getObject()>0);
				setVisible(showPreviousButton  && (!hiddenPreviousButton || pageNumberModel.getObject()>0));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setPage(pageNumberModel.getObject()-1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, pageNumberModel);
			}
			
		}));
		add(new SquaresContainer("next"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(pageNumberModel.getObject()<getTotalPages()-1);
				setVisible(showNextButton  && (!hiddenNextButton || pageNumberModel.getObject()<(getTotalPages()-1)));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setPage(pageNumberModel.getObject()+1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, pageNumberModel);
			}
			
		}));
		add(new SquaresContainer("last"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(pageNumberModel.getObject()<getTotalPages()-1);
				setVisible(showLastButton  && (!hiddenLastButton || threshold<(getTotalPages()-pagesToShow)));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				setPage(getTotalPages()-1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, pageNumberModel);
			}
			
		}));
		add(new WebMarkupContainer("previousPages"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showMorePagesInformation && threshold>0);
			}
		});
		add(new WebMarkupContainer("nextPages"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showMorePagesInformation && threshold<(getTotalPages()-pagesToShow));
			}
		});
		
		IModel<Integer> pages = new LoadableDetachableModel<Integer>() {
			@Override
			protected Integer load() {
				return Math.min(getTotalPages(), pagesToShow);
			}
		};
		
		add(new Loop("page", pages) {
			
			@Override
			protected void populateItem(final LoopItem item) {
				item.add(AttributeModifier.append("class", new LoadableDetachableModel<String>() {

					@Override
					protected String load() {
						return item.getIndex()+threshold==pageNumberModel.getObject() ? "active" : "";
					}
				}));
				item.add(new AjaxLink<Void>("link") {
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
					};

					@Override
					public void onClick(AjaxRequestTarget target) {
						setPage(item.getIndex()+threshold);
						target.add(BootstrapPaginator.this);
						onPageChange(target, pageNumberModel);
					}
				}.add(new Label("label", new LoadableDetachableModel<Integer>() {

					@Override
					protected Integer load() {
						return item.getIndex()+threshold+1;
					}
				})));
			}
		});
	}
	
	private void setPage(int page){
		pageNumberModel.setObject(page);
		if(page>pagesToShow-2) threshold=(int) Math.max(Math.min(page-(Math.floor(pagesToShow/2)), getTotalPages()-pagesToShow), 0);
		if(page>getTotalPages()-pagesToShow) threshold=getTotalPages()-pagesToShow;
		if(page<pagesToShow-1) threshold=0;
	}
	
	private class SquaresContainer extends WebMarkupContainer {
		public SquaresContainer(String id) {
			super(id);
			add(AttributeModifier.append("class", new LoadableDetachableModel<String>() {
				@Override
				protected String load() {
					return isEnabled() ? "" : "disabled";
				}
			}));
		}
		
	}

	@Override
	public IModel<Integer> getModel() {
		return pageNumberModel;
	}

	@Override
	public Integer getModelObject() {
		return pageNumberModel.getObject();
	}

	@Override
	public void setModel(IModel<Integer> model) {
		pageNumberModel=model;
		setPage(pageNumberModel.getObject());
	}

	@Override
	public void setModelObject(Integer page) {
		pageNumberModel.setObject(page);
		setPage(pageNumberModel.getObject());
	}

	public boolean isShowFirstButton() {
		return showFirstButton;
	}

	public void setShowFirstButton(boolean showFirstButton) {
		this.showFirstButton = showFirstButton;
	}

	public boolean isShowLastButton() {
		return showLastButton;
	}

	public void setShowLastButton(boolean showLastButton) {
		this.showLastButton = showLastButton;
	}

	public boolean isShowNextButton() {
		return showNextButton;
	}

	public void setShowNextButton(boolean showNextButton) {
		this.showNextButton = showNextButton;
	}

	public boolean isShowPreviousButton() {
		return showPreviousButton;
	}

	public void setShowPreviousButton(boolean showPreviousButton) {
		this.showPreviousButton = showPreviousButton;
	}

	public int getPagesToShow() {
		return pagesToShow;
	}

	public void setPagesToShow(int pagesToShow) {
		this.pagesToShow = pagesToShow;
		setPage(pageNumberModel.getObject());
	}

	public int getTotalPages() {
		return (int) Math.ceil(totalResults.getObject()/ (double) numberResultsPerPage);
	}

	public boolean isHiddenFirstButton() {
		return hiddenFirstButton;
	}

	public void setHiddenFirstButton(boolean hiddenFirstButton) {
		this.hiddenFirstButton = hiddenFirstButton;
	}

	public boolean isHiddenLastButton() {
		return hiddenLastButton;
	}

	public void setHiddenLastButton(boolean hiddenLastButton) {
		this.hiddenLastButton = hiddenLastButton;
	}

	public boolean isHiddenPreviousButton() {
		return hiddenPreviousButton;
	}

	public void setHiddenPreviousButton(boolean hiddenPreviousButton) {
		this.hiddenPreviousButton = hiddenPreviousButton;
	}

	public boolean isHiddenNextButton() {
		return hiddenNextButton;
	}

	public void setHiddenNextButton(boolean hiddenNextButton) {
		this.hiddenNextButton = hiddenNextButton;
	}

	
	public abstract void onPageChange(AjaxRequestTarget target, IModel<Integer> page);

	public IModel<Integer> getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(IModel<Integer> totalResults) {
		this.totalResults = totalResults;
	}

	public int getNumberResultsPerPage() {
		return numberResultsPerPage;
	}

	public void setNumberResultsPerPage(int numberResultsPerPage) {
		this.numberResultsPerPage = numberResultsPerPage;
	}
	public int getCurrentIndex(){
		return this.pageNumberModel.getObject() * numberResultsPerPage;
	}
}
