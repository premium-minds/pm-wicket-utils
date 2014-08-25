/*
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