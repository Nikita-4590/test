//CREATE OBJECT BODY TABLE
(function() {
	me.lib.TableBody = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'tbody', node);
		} else {
			$UI.call(this, 'tbody');
		}
		
	};
	
	me.lib.TableBody.displayName = 'tbody';
	me.lib.TableBody.prototype = Object.create($UI.prototype);
	/*
	 * find all attr of body(current page, total page, data search 
	 */
	me.lib.TableBody.prototype.restoreDataSearch = function(tbody, stored_data_search) {
		var _this = this;
		var _checkDirection = false;
		var _checkSort = false;
		$(tbody).each(function() {
			$.each(this.attributes, function() {
				if(this.specified) {
					if(this.name == 'total-page') {
						_this.$parent.totalPage = $(tbody).attr('total-page');
					} else if(this.name == 'current-page') {
						_this.$parent.page = parseInt($(tbody).attr('current-page')) + 1;
					} else if(this.name == 'sort') {
						_this.$parent.head.reloadSortControls();
						_this.$parent.sort = $(tbody).attr('sort');
						if(isSet(_this.$parent.config.default_sort)) {
							_checkSort = true;
							_this.$parent.config.default_sort = $(tbody).attr('sort');
						}
					} else if(this.name == 'direction') {
						_checkDirection = true;
						_this.$parent.direction = $(tbody).attr('direction');
					} else {
						if(isUnset(_this.$parent.queries)) {
							_this.$parent.queries = new Array();
						}
						
						if(isArray(stored_data_search)) {
							for(var index = 0; index < stored_data_search.length ; index++) {
								if(this.name == stored_data_search[index]['id']) {
									_this.$parent.queries.push({'query_name' : stored_data_search[index]['query_name'], 'query_value' : $(tbody).attr(this.name)});
									$('#' + stored_data_search[index]['id']).val($(tbody).attr(this.name));
									break;
								}
							}
						}
					}
				}
			});
		});
		if(isUnset(_this.$parent.totalPage)) {
			_this.$parent.totalPage = 1;
		}
		if(_checkDirection && _checkSort) {
			_this.$parent.head.reloadSortAfterReloadBody();
		}
	};
	/*
		create tbody from html string
		data input parameter :  +	String as '<tbody><tr>....</tr><tr>....</tr></tbody>'
	*/

	me.lib.TableBody.prototype.createBodyFromHTML = function(tbodyHTML) {
		var _this = this;
		var children = $(tbodyHTML).find('tr');
		if(children.length > 0) {
			for(var index = 0; index < children.length; index++) {
				var tr = $UI.createDomFromHTML('tr', children[index]);
				if (isFunction(_this.$parent.config.row_selected)) {
					tr.applyCss('dapps-table-row-hoverable');
					$(tr.htmlElement).click(function() {
						_this.$parent.config.row_selected(this);
					});
				}
				_this.appendChild(tr);
			}
		} else {
			_this.createBodyNoData();
		}
	};
	
	/*
		create body from list node tr
		data input parameter : 	Array[me.lib.table_row]
	*/
	me.lib.TableBody.prototype.createBodyFromNode = function(tbodyNodes) {
		var _this = this;
		if(tbodyNodes.length != 0) {
			for (var index = 0; index < tbodyNodes.length; index++) {
				_this.appendChild(tbodyNodes[index]);
				_this.children.push(tbodyNodes[index]);
			}
		} else {
			_this.createBodyNoData();
		}
		
	};
	/*
		generated body if this table body no data
	*/
	
	me.lib.TableBody.prototype.createBodyNoData = function() {
		var _this = this;
		var offset = _this.$parent.config.scrollable ? 1 : 0;
		var noDataCell = new me.lib.TableTd();
		noDataCell.applyCss('dapps-table-no-data');
		noDataCell.setValue(_this.$parent.config.no_data_message);
		var noDataRow = new me.lib.TableRow();
		noDataRow.applyCss('dapps-table-no-data-row');
		noDataRow.appendChild(noDataCell);
		_this.appendChild(noDataRow);
		noDataCell.applyAttr('colspan', _this.$parent.head.count_column);
	};
	me.lib.TableBody.prototype.searchLocal = function() {
		
	};
	me.lib.TableBody.prototype.hide = function() {
		for(var index = 0; index < this.children.length; index++) {
			
		}
	};
	me.lib.TableBody.prototype.show = function() {
		
	};
	
	
	window.me.lib.TableBody = me.lib.TableBody;
})();



