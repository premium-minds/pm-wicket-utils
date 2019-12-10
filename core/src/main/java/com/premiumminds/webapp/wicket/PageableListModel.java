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

import java.util.Collection;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public abstract class PageableListModel<T extends Collection<?>> implements IModel<T>{
	private static final long serialVersionUID = 5067687224410674672L;
	private T list;
	private int currentStartIndex;
	private int currentViewSize;
	private int size;
	
	public PageableListModel(int size){
		this.size = size;
	}
	
	public abstract T getPageList(int startIndex, int viewSize);
	
	public void setObject(T object) { throw new UnsupportedOperationException("can't set object"); };
	public T getObject(){
		if(list==null) list = getPageList(currentStartIndex, currentViewSize);
		return list;
	}
	
	public void setStartIndex(int startIndex, int viewSize){
		if(startIndex != currentStartIndex || viewSize != currentViewSize){
			list = getPageList(startIndex, viewSize);
			currentStartIndex = startIndex;
			currentViewSize = viewSize;
		}
	}
	
	public int getStartIndex(){
		return currentStartIndex;
	}
	
	public int getViewSize(){
		return currentViewSize;
	}
	
	public int getSize(){
		return size;
	}
	
	public void detach() {
		if(list instanceof IDetachable){
			((IDetachable) list).detach();
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + currentStartIndex;
		result = prime * result + currentViewSize;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageableListModel<?> other = (PageableListModel<?>) obj;
		if (currentStartIndex != other.currentStartIndex)
			return false;
		if (currentViewSize != other.currentViewSize)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		return true;
	}
}
