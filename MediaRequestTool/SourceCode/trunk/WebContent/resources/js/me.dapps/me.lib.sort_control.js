(function() {
	me.lib.SortControl = function(defaultSort, thisSortValue) {
		this.defaultSort = defaultSort;
		this.sort = thisSortValue;
		$UI.call(this, 'div');
		this.applyCss('dapps-table-header-sort-control');
	};
	me.lib.SortControl.displayName = 'div';
	me.lib.SortControl.prototype = Object.create($UI.prototype);
	
	/*
		set value for sort controls if it is columns have sort default
	*/
	me.lib.SortControl.prototype.applySortDefault = function() {
		if(this.defaultSort) {
			this.applyCss('dapps-table-sort-control-desc');
			this.direction = 'desc';
		}
		
	};
	/*
		apply sort for sort controls
		 + parameter is current sort value of table : example : id, status....
		 + check if table is sorting with this SortControl
		 + set current sort, direction & css class for sort controls
		 
	*/
	me.lib.SortControl.prototype.applySort = function(sortValue) {
		if(sortValue == this.sort) {
			this.direction = this.direction == 'desc' ? 'asc' : 'desc';
		} else if(isSet(this.direction)) {
			this.direction = this.direction == 'asc' ? 'desc' : 'asc';
		} else {
			this.direction = 'desc';
		}
		$(this.htmlElement).removeClass('dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-desc').addClass('dapps-table-sort-control-' + this.direction);
	};
	
	me.lib.SortControl.prototype.resetSortControl = function(sortValue) {
		if(this.sort != sortValue) {
			this.direction = undefined;
		}
	};
	/*
	 * reload sort when search
	 * parameter : sort_default after search
	 */
	me.lib.SortControl.prototype.reloadSort = function(sortValue, direction) {
		this.defaultSort = sortValue == this.sort ? true : false;
		if(sortValue == this.sort) {
			if(isSet(direction)) {
				this.direction = direction;
			} else {
				this.direction = 'desc';
			}
			
			$(this.htmlElement).addClass('dapps-table-sort-control-' + this.direction);
		}
		
	};
	window.me.lib.SortControl = me.lib.SortControl;
})();

(function() {
	me.lib.Div = function(defaultSort) {
		$UI.call(this, 'div');
	};
	me.lib.Div.prototype = Object.create($UI.prototype);
	
	me.lib.Div.prototype.setValue = function(value) {
		$(this.htmlElement).html(value);
	};
	window.me.lib.Div = me.lib.Div;
})();

