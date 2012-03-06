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

