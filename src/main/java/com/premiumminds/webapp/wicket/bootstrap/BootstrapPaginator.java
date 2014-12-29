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

import org.apache.wicket.Application;
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
 * This component creates a paginator component using the bootstrap pagination element.
 * 
 * <p>
 * Example:
 * 
 * <pre>
 * &lt;nav&gt;
 *   &lt;ul wicket:id=&quot;paginator&quot; class=&quot;pagination&quot;&gt;&lt;/ul&gt;
 * &lt;/nav&gt;
 * </pre>
 * 
 * <p>
 * The related Java code:
 * 
 * <pre>
 * new BootstrapPaginator(&quot;paginator&quot;, Model.of(2000)) {
 *     public void onPageChange(AjaxRequestTarget target, IModel&lt;Integer&gt; page) {
 *        ...
 *     }
 * }
 * </pre>
 * 
 * @author acamilo
 * @see <a href="http://getbootstrap.com/components/#pagination">bootstrap pagination</a>
 */
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
	private IModel<Integer> totalResults;
	private int numberResultsPerPage;
	
	private boolean stripTags; // hack to not render wicket tags

	/**
	 * @deprecated As of release 2.6, replaced by {@link #BootstrapPaginator(String, IModel)}
	 */
	@Deprecated
	public BootstrapPaginator(String id) {
		this(id, Model.of(100), 20);
	}
	
	/**
	 * Creates a paginator with 20 results per page by default
	 * 
	 * @param id component id
	 * @param totalResults model for total results
	 */
	public BootstrapPaginator(String id, IModel<Integer> totalResults){
		this(id, totalResults, 20);
	}

	/**
	 * Creates a paginator
	 * 
	 * @param id component id
	 * @param totalResults model for total results
	 * @param resultsPerPage number of results per page
	 */
	@SuppressWarnings("serial")
	public BootstrapPaginator(String id, IModel<Integer> totalResults, int resultsPerPage){
		super(id, Model.of(0));
		stripTags = Application.get().getMarkupSettings().getStripWicketTags();
		setOutputMarkupId(true);
		
		this.totalResults = totalResults;
		this.numberResultsPerPage = resultsPerPage;

		add(new SquaresContainer("first"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(getModelObject()>0);
				setVisible(showFirstButton && (!hiddenFirstButton || getThreshold()>0));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				BootstrapPaginator.this.setModelObject(0);
				target.add(BootstrapPaginator.this);
				onPageChange(target, BootstrapPaginator.this.getModel());
			}
			
		}));
		add(new SquaresContainer("previous"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(BootstrapPaginator.this.getModelObject()>0);
				setVisible(showPreviousButton  && (!hiddenPreviousButton || BootstrapPaginator.this.getModelObject()>0));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				BootstrapPaginator.this.setModelObject(BootstrapPaginator.this.getModelObject()-1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, BootstrapPaginator.this.getModel());
			}
			
		}));
		add(new SquaresContainer("next"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(BootstrapPaginator.this.getModelObject()<getTotalPages()-1);
				setVisible(showNextButton  && (!hiddenNextButton || BootstrapPaginator.this.getModelObject()<(getTotalPages()-1)));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				BootstrapPaginator.this.setModelObject(BootstrapPaginator.this.getModelObject()+1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, BootstrapPaginator.this.getModel());
			}
			
		}));
		add(new SquaresContainer("last"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setEnabled(BootstrapPaginator.this.getModelObject()<getTotalPages()-1);
				setVisible(showLastButton  && (!hiddenLastButton || getThreshold()<(getTotalPages()-pagesToShow)));
			}
		}.add(new AjaxLink<Void>("link"){
			@Override
			public void onClick(AjaxRequestTarget target) {
				BootstrapPaginator.this.setModelObject(getTotalPages()-1);
				target.add(BootstrapPaginator.this);
				onPageChange(target, BootstrapPaginator.this.getModel());
			}
			
		}));
		add(new WebMarkupContainer("previousPages"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showMorePagesInformation && getThreshold()>0);
			}
		});
		add(new WebMarkupContainer("nextPages"){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(showMorePagesInformation && getThreshold()<(getTotalPages()-pagesToShow));
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
				final int threshold = getThreshold();
				item.add(AttributeModifier.append("class", new LoadableDetachableModel<String>() {

					@Override
					protected String load() {
						return item.getIndex()+getThreshold()==BootstrapPaginator.this.getModelObject() ? "active" : "";
					}
				}));
				item.add(new AjaxLink<Void>("link") {
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
					};

					@Override
					public void onClick(AjaxRequestTarget target) {
						BootstrapPaginator.this.setModelObject(item.getIndex()+threshold);
						target.add(BootstrapPaginator.this);
						onPageChange(target, BootstrapPaginator.this.getModel());
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
	
	private int getThreshold(){
		if(getTotalPages()<=getModelObject()) setModelObject(0);
		
		if(getModelObject()>pagesToShow-2) return (int) Math.max(Math.min(getModelObject()-(Math.floor(pagesToShow/2)), getTotalPages()-pagesToShow), 0);
		if(getModelObject()>getTotalPages()-pagesToShow) return Math.max(getTotalPages()-pagesToShow, 0);
		return 0;
	}
	
	@SuppressWarnings("serial")
	private class SquaresContainer extends WebMarkupContainer {
		public SquaresContainer(String id) {
			super(id);
			add(AttributeModifier.append("class", new LoadableDetachableModel<String>() {
				private static final long serialVersionUID = 4330457477794180592L;

				@Override
				protected String load() {
					return isEnabled() ? "" : "disabled";
				}
			}));
		}
		
	}

	// hack to remove wicket:panel tag from output
	@Override
	protected void onBeforeRender() {
		Application.get().getMarkupSettings().setStripWicketTags(true);
		super.onBeforeRender();
	}
	
	// hack to remove wicket:panel tag from output
	@Override
	protected void onAfterRender() {
		Application.get().getMarkupSettings().setStripWicketTags(stripTags);
		super.onAfterRender();
	}
	
	/**
	 * Get the model with the number of the current page
	 * 
	 * @return model of the current page
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IModel<Integer> getModel() {
		return (IModel<Integer>) getDefaultModel();
	}

	/**
	 * Get the number of the current page
	 * 
	 * @return the current page
	 */
	@Override
	public Integer getModelObject() {
		return (Integer) getDefaultModelObject();
	}

	/**
	 * Set the model with the number of the current page
	 * 
	 * @param model model of the current page
	 */
	@Override
	public void setModel(IModel<Integer> model) {
		setDefaultModel(model);
	}

	/**
	 * Set the number of the current page
	 * 
	 * @param page current page
	 */
	@Override
	public void setModelObject(Integer page) {
		setDefaultModelObject(page);
	}

	/**
	 * Return if the goto first page button is shown
	 */
	public boolean isShowFirstButton() {
		return showFirstButton;
	}

	/**
	 * Sets whether to show the goto first page option
	 */
	public void setShowFirstButton(boolean showFirstButton) {
		this.showFirstButton = showFirstButton;
	}

	/**
	 * Return if the goto last page button is shown
	 */
	public boolean isShowLastButton() {
		return showLastButton;
	}

	/**
	 * Sets whether to show the goto last page option
	 * 
	 * @param showLastButton if true show the goto last page option
	 */
	public void setShowLastButton(boolean showLastButton) {
		this.showLastButton = showLastButton;
	}

	/**
	 * Return if the goto next page button is shown
	 */
	public boolean isShowNextButton() {
		return showNextButton;
	}

	/**
	 * Sets whether to show the goto next page option
	 */
	public void setShowNextButton(boolean showNextButton) {
		this.showNextButton = showNextButton;
	}

	/**
	 * Return if the goto previous page button is shown 
	 */
	public boolean isShowPreviousButton() {
		return showPreviousButton;
	}

	/**
	 * Sets whether to show the goto previous page option
	 */
	public void setShowPreviousButton(boolean showPreviousButton) {
		this.showPreviousButton = showPreviousButton;
	}

	/**
	 * Return the number of pages to show on the pagination component 
	 */
	public int getPagesToShow() {
		return pagesToShow;
	}

	/**
	 * Sets the number of pages to show on the pagination component 
	 */
	public void setPagesToShow(int pagesToShow) {
		this.pagesToShow = pagesToShow;
	}

	/**
	 * Get the total of pages shown
	 * 
	 * @return totalResults / numberOfResultsPerPage
	 */
	public int getTotalPages() {
		return (int) Math.ceil(totalResults.getObject()/ (double) numberResultsPerPage);
	}

	/**
	 * Return if the goto first page button is hidden when it's not needed
	 */
	public boolean isHiddenFirstButton() {
		return hiddenFirstButton;
	}

	/**
	 * Sets if the goto first page button should be hidden when it's not needed
	 */
	public void setHiddenFirstButton(boolean hiddenFirstButton) {
		this.hiddenFirstButton = hiddenFirstButton;
	}

	/**
	 * Return if the goto last page button is hidden when it's not needed
	 */
	public boolean isHiddenLastButton() {
		return hiddenLastButton;
	}

	/**
	 * Sets if the goto last page button should be hidden when it's not needed
	 */
	public void setHiddenLastButton(boolean hiddenLastButton) {
		this.hiddenLastButton = hiddenLastButton;
	}

	/**
	 * Return if the goto previous page button is hidden when it's not needed
	 */
	public boolean isHiddenPreviousButton() {
		return hiddenPreviousButton;
	}

	/**
	 * Sets if the goto previous page button should be hidden when it's not needed
	 */
	public void setHiddenPreviousButton(boolean hiddenPreviousButton) {
		this.hiddenPreviousButton = hiddenPreviousButton;
	}

	/**
	 * Return if the goto next page button is hidden when it's not needed
	 */
	public boolean isHiddenNextButton() {
		return hiddenNextButton;
	}

	/**
	 * Sets if the goto next page button should be hidden when it's not needed
	 */
	public void setHiddenNextButton(boolean hiddenNextButton) {
		this.hiddenNextButton = hiddenNextButton;
	}

	/**
	 * This method is called every time the current page changes
	 * 
	 * @param target ajax target (paginator was already added)
	 * @param page the new current page
	 */
	public abstract void onPageChange(AjaxRequestTarget target, IModel<Integer> page);

	/**
	 * Get the model with the total number of results
	 * 
	 * @return model with number of results
	 */
	public IModel<Integer> getTotalResults() {
		return totalResults;
	}

	/**
	 * Set the model with the total number of results
	 * 
	 * @param totalResults model with the number of results
	 */
	public void setTotalResults(IModel<Integer> totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * Get the number of results per page that the paginator uses to calculate how many pages should exist
	 * 
	 * @return number of results per page
	 */
	public int getNumberResultsPerPage() {
		return numberResultsPerPage;
	}

	/**
	 * Set the number of results per page that the paginator uses to calculate how many pages should exist
	 * 
	 * @param numberResultsPerPage number of results per page
	 */
	public void setNumberResultsPerPage(int numberResultsPerPage) {
		this.numberResultsPerPage = numberResultsPerPage;
	}
	
	/**
	 * Get the first first index for the current page
	 * 
	 * @return page * numberOfResultsPerPage
	 */
	public int getCurrentIndex(){
		return getModelObject() * numberResultsPerPage;
	}
}
