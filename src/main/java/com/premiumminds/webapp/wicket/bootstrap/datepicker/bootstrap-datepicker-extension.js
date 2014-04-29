(function($, undefined){
	var extensionMethods = {
		placeOld: $.fn.datepicker.Constructor.prototype.place,
		place: function(){
			this.placeOld();
			var parentZIndexes = this.element.parents().map(function() {
				return parseInt($(this).css('z-index')) || 0;
			});
			var zIndex = Math.max.apply(Math, Array.prototype.slice.apply(parentZIndexes)) + 10;
			this.picker.css({
				zIndex: zIndex
			});
		}
	}
	
	$.extend(true, $.fn.datepicker.Constructor.prototype, extensionMethods);
}(window.jQuery));