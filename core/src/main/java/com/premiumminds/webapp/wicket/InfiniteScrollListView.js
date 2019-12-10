/*
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
var infiniteScrollInstances = {};

function infiniteScrollPrepare(componentId){
	var componentElement = Wicket.$(componentId);
	
	var newComponent = componentElement.cloneNode(true);
	newComponent.removeAttribute("id");
	
	componentElement.parentNode.insertBefore(newComponent, componentElement);
	
	Wicket.hide(componentElement);
	
	return true;
}

function infinitScrollSuccess(componentId){
	Wicket.show(componentId);
}


var InfiniteScroll = function(containerId){
	this.upUrl = null;
	this.downUrl = null;
	this.upEnabled = null;
	this.downEnabled = null;
	this.scrollContainer = null;
	this.mayScroll = true;
	
	this.changeUp = function(enabled){ this.upEnabled = enabled; };
	this.changeDown = function(enabled){ this.downEnabled = enabled; };
	this.setUrls = function(upUrl, downUrl){ this.upUrl = upUrl; this.downUrl = downUrl; };
	
	this.internalHandleScroll = function(scrollContainerId){
		this.scrollContainer = Wicket.$(scrollContainerId);
		
		if(this.scrollContainer.scrollTop==0 && this.upEnabled && this.mayScroll){
			mayScroll=false;
			wicketAjaxGet(this.upUrl);
		}

		if(this.scrollContainer.scrollTop+this.scrollContainer.clientHeight>=this.scrollContainer.scrollHeight && this.downEnabled && this.mayScroll){
			mayScroll = false;
			wicketAjaxGet(this.downUrl);
		}
	}
	
	this.scrollUpTo = function(componentId){
		var component = Wicket.$(componentId);
		if(component!=null){
			component.scrollIntoView(false);
		}
	}

	this.scrollDownTo = function(componentId){
		var component = Wicket.$(componentId);
		if(component!=null){
			component.scrollIntoView(true);
		}
	}
};

InfiniteScroll.handleScroll = function(containerId){
	InfiniteScroll.getFromContainer(containerId).internalHandleScroll(containerId);
};

InfiniteScroll.getFromContainer = function(containerId){
	if(!infiniteScrollInstances.hasOwnProperty(containerId)){
		infiniteScrollInstances[containerId] = new InfiniteScroll(containerId);
	}
	
	return infiniteScrollInstances[containerId];
}

