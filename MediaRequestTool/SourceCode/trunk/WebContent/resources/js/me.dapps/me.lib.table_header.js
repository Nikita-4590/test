
//CREATE OBJECT HEADER TABLE
(function() {
	me.lib.TableHeader = function() {
		$UI.call(this, 'thead');
		
	};
	me.lib.TableHeader.displayName = 'thead';
	me.lib.TableHeader.prototype = Object.create($UI.prototype);
	
	/*
	 * generated header from html String
	 * parameter is String and have construct as : '<thead><tr><th>.....</th></tr></thead>'
	 */
	me.lib.TableHeader.prototype.createHeaderFromHTML = function(thead) {
		var _this = this;
		var rows = $(thead).find('tr');
		var table_config = _this.$parent.config;
		for(var index = 0; index < rows.length ; index++) {
			var tr = new me.lib.TableRow();
			_this.appendChild(tr);
			var cells = $(rows[index]).find('th');
			_this.count_column = cells.length;
			for(var indexCells = 0 ; indexCells < cells.length; indexCells++) {
				var cssClass = $(cells[indexCells]).attr('class');
				var th = new me.lib.TableTh();
				th.applyCss(cssClass);
				tr.appendChild(th);
				var div = new me.lib.Div();
				th.appendChild(div);
				div.applyCss('dapps-table-header-text');
				div.setValue($(cells[indexCells]).html());
				if(table_config.sortable && isSet(table_config.sort_data) && table_config.sort_data.length == cells.length && (table_config.sort_data[indexCells]) && table_config.sort_data[indexCells].length != 0) {
					var isDefault = false;
					if(table_config.sort_data[indexCells] == table_config.default_sort) {
						isDefault = true;
						_this.sort = table_config.sort_data[indexCells];
					}
					
					var divSort = new me.lib.SortControl(isDefault, table_config.sort_data[indexCells]);
					th.appendChild(divSort);
					$(divSort.htmlElement).click(function(e) {
						e.preventDefault();
						_this.removeSortCss();
						this.$ui.applySort(this.$ui.sort);
						if(_this.$parent.config.isResetSortAfterSearch) {
							for(var index = 0; index < tr.children.length; index++) {
								_this.reloadSortActionClick(tr.children[index], this.$ui.sort);
							}
						}
						_this.direction = this.$ui.direction;
						_this.sort = this.$ui.sort;
						if(isFunction(_this.onsorting)) {
							_this.onsorting(_this);
						}
					});
					divSort.applySortDefault();
				}
				
			}
		}
	};
	
	/*
	 * reload class sort of list th(sort controls) when user click sort in the header
	 */
	
	me.lib.TableHeader.prototype.reloadSortActionClick = function(thElement, sortValue) {
		for(var index = 0; index < thElement.children.length; index ++) {
			if(isFunction(thElement.children[index].resetSortControl)) {
				thElement.children[index].resetSortControl(sortValue);
			}
		}
	};
	/*
	 * reload class sort of reload sort controls in the header
	 */
	
	me.lib.TableHeader.prototype.reloadSortControls = function() {
		var _this = this;
		_this.removeSortCss();
		for(var index = 0; index < _this.children.length; index++) {
			var rowChildren = _this.children[index];
			for(var indexRow = 0; indexRow < rowChildren.children.length; indexRow++) {
				var divChildren = rowChildren.children[indexRow].children;
				for(var divIndex = 0; divIndex < divChildren.length; divIndex++) {
					if(isFunction(divChildren[divIndex].reloadSort)) {
						divChildren[divIndex].cleanSortControl();
						divChildren[divIndex].reloadSort(_this.$parent.sort);
					}
				}
			}
		}
	};
	
	
	
	me.lib.TableHeader.prototype.reloadSortAfterReloadBody = function() {
		var _this = this;
		_this.removeSortCss();
		for(var index = 0; index < _this.children.length; index++) {
			var rowChildren = _this.children[index];
			for(var indexRow = 0; indexRow < rowChildren.children.length; indexRow++) {
				var divChildren = rowChildren.children[indexRow].children;
				for(var divIndex = 0; divIndex < divChildren.length; divIndex++) {
					if(isFunction(divChildren[divIndex].reloadSort)) {
						divChildren[divIndex].reloadSort(_this.$parent.sort, _this.$parent.direction);
					}
				}
			}
		}
	};
	
	/*
	 * remove sort all class sort of sort controls
	*/
	
	
	me.lib.TableHeader.prototype.removeSortCss = function() {
		$(this.htmlElement).find('.dapps-table-sort-control-desc').removeClass('dapps-table-sort-control-desc');
		$(this.htmlElement).find('.dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-asc');
	};
	
	window.me.lib.TableHeader = me.lib.TableHeader;
})();


