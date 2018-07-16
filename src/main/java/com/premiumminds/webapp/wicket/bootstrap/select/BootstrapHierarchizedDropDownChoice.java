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
package com.premiumminds.webapp.wicket.bootstrap.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;

@SuppressWarnings("rawtypes")
public class BootstrapHierarchizedDropDownChoice<T extends IHierarchyValue> extends
		BootstrapDropDownChoice<T> {
	private static final long serialVersionUID = 1437604672358804420L;
	
	private boolean disableParents = false;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 */
	public BootstrapHierarchizedDropDownChoice(final String id)
	{
		super(id);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 */
	public BootstrapHierarchizedDropDownChoice(final String id, final List<? extends T> choices)
	{
		super(id, choices);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 */
	public BootstrapHierarchizedDropDownChoice(final String id, final List<? extends T> choices,
		final IChoiceRenderer<? super T> renderer)
	{
		super(id, choices, renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 */
	public BootstrapHierarchizedDropDownChoice(final String id, IModel<T> model, final List<? extends T> choices)
	{
		super(id, model, choices);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 * @param renderer
	 *            The rendering engine
	 */
	public BootstrapHierarchizedDropDownChoice(final String id, IModel<T> model, final List<? extends T> choices,
		final IChoiceRenderer<? super T> renderer)
	{
		super(id, model, choices, renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 */
	public BootstrapHierarchizedDropDownChoice(String id, IModel<? extends List<? extends T>> choices)
	{
		super(id, choices);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 */
	public BootstrapHierarchizedDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices)
	{
		super(id, model, choices);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 * @param renderer
	 *            The rendering engine
	 */
	public BootstrapHierarchizedDropDownChoice(String id, IModel<? extends List<? extends T>> choices,
		IChoiceRenderer<? super T> renderer)
	{
		super(id, choices, renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 * @param renderer
	 *            The rendering engine
	 */
	public BootstrapHierarchizedDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices,
		IChoiceRenderer<? super T> renderer)
	{
		super(id, model, choices, renderer);
	}

	@Override
	protected void setOptionAttributes(AppendingStringBuffer buffer, T choice,
			int index, String selected) {
		super.setOptionAttributes(buffer, choice, index, selected);
		int depth = calculateDepth(choice);
		buffer.append(" class=\"treedepth").append(depth).append("\"");
		if(disableParents && choice.getChildren()!=null && !choice.getChildren().isEmpty())
			buffer.append(" disabled=\"disabled\"");
	}
	
	@Override
	public IModel<? extends List<? extends T>> getChoicesModel() {
		List<? extends T> choices = convertChoices(super.getChoices());
		return (IModel<? extends List<? extends T>>) Model.of(choices);
	}
	
	public void setDisableParents(boolean disable){
		this.disableParents = disable;
	}
	
	public boolean isDisableParents(){
		return this.disableParents;
	}

	@SuppressWarnings("unchecked")
	private static <T extends IHierarchyValue> List<T> convertChoices(Collection<T> choices){
		ArrayList<T> newList = new ArrayList<T>();
		for(T elem : choices){
			newList.add(elem);
			if(elem.getChildren()!=null && !elem.getChildren().isEmpty()){
				newList.addAll((Collection<? extends T>) convertChoices(elem.getChildren()));
			}
		}
		
		return newList;
	}
	
	private static <T extends IHierarchyValue> int calculateDepth(T option){
		if(option.getParent()==null) return 0;
		else return 1+calculateDepth(option.getParent());
	}
}
