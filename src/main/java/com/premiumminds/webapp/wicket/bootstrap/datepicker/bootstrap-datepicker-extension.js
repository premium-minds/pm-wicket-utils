(function($, undefined){
	var extensionMethods = {
		placeOld: $.fn.datepicker.Constructor.prototype.place,
		_triggerOld: $.fn.datepicker.Constructor.prototype._trigger,
		place: function(){
			this.placeOld();
			var parentZIndexes = this.element.parents().map(function() {
				return parseInt($(this).css('z-index')) || 0;
			});
			var zIndex = Math.max.apply(Math, Array.prototype.slice.apply(parentZIndexes)) + 10;
			this.picker.css({
				zIndex: zIndex
			});
		},
		_trigger: function(event, altdate){
			if(event=='show' || event=='hide') return;
			this._triggerOld(event, altdate);
		}
	}
	
	$.extend(true, $.fn.datepicker.Constructor.prototype, extensionMethods);
}(window.jQuery));