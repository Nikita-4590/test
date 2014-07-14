/*
 * create abstract object table
 */

(function() {
	
	var $UI = function(tagName, node) {
		this.tagName = tagName;
		this.children = new Array();
		this.node = node;
		if(isSet(node) && node instanceof Node) {
			this.htmlElement = node;
			this.htmlElement.$ui = this;
	} else if(isSet(tagName)) {
			this.htmlElement = document.createElement(tagName);
			this.htmlElement.$ui = this;
		} else {
			$(this.htmlElement).html(node);
		}
	};
	/*
		* return a ui with list children
		* recursive function
		* parameter is : tagName
		                 string html
	*/
	
	$UI.createDomFromHTML = function(tagName, node) {
		
		if(isSet(node) && node instanceof Node) {
			var myLibXtag = me.lib;
			var xtagFunction = undefined;
			for(var key in myLibXtag) {
				if(tagName == myLibXtag[key].displayName) {
					xtagFunction = key;
					break;
				}
			}
			
			var meUI = new window['me']['lib'][xtagFunction](this.tagName);
			meUI.applyAllAttr(node);
			var checkHasChild = false;
			for(var key in myLibXtag) {
				var xtag = myLibXtag[key].displayName;
				if(isSet(xtag)) {
					var children = $(node).find(xtag);
					for(var index = 0; index < children.length; index++) {
						var childrenUi = $UI.createDomFromHTML(xtag, children[index]);
						meUI.appendChild(childrenUi);
						checkHasChild = true;
					}
				}
			}
			
			if(!checkHasChild) {
				$(meUI.htmlElement).text($(node).text());
			}
			return meUI;
		}
				
	};
	
	$UI.prototype.appendChild = function(childNode) {
		
		try {
			
			if(childNode instanceof $UI) {
				childNode.$parent = this;
				this.htmlElement.appendChild(childNode.htmlElement);
				this.children.push(childNode);
			} else {
				this.htmlElement.appendChild(childNode);
			}
			
		} catch(err) {
			console.log(err);
			//TODO throws exception
		}
	};
	
	$UI.prototype.parseChildren = function() {
		
	};
	
	$UI.prototype.appendChildren = function(childenNode) {
		
		var $this = this;
		if(isArray(childrenNode)) {
			$.each(childrenNode, function(index) {
				if(childrenNode[index] instanceof $UI) {
					childenNode[index].$parent = $this;
					$this.htmlElement.appendChild(childrenNode[index]);
					$this.children.push(childrenNode[index]);
				}
			});
		}
		
	};
	
	$UI.prototype.applyCss = function(cssClass) {
		$(this.htmlElement).addClass(cssClass);
	};
	
	$UI.prototype.removeCss = function(cssClass) {
		$(this.htmlElement).removeClass(cssClass);
	};
		
	$UI.prototype.removeChild = function(childNode) {
		var array = new Array();
		this.children.forEach(function(index) {
			if(this.children[index] != childNode) {
				array.push(this.children[index]);
			}
		});
		this.children = array;
		
	};
	
	$UI.prototype.applyAttr = function(attrName, attrValue) {
		$(this.htmlElement).attr(attrName, attrValue);
	};
	
	$UI.prototype.applyAllAttr = function(node) {
		
		var _this = this;
		$(node).each(function() {
			$.each(this.attributes, function() {
				if(this.specified) {
					if(this.name == 'class') {
						_this.applyCss(this.value);
					} else {
						_this.applyAttr(this.name, this.value);
					}
					
				}
			});
		});
	};
	
	$UI.prototype.getText = function() {
		return $(this.htmlElement).text();
	};
	
	// export
	window.$UI = $UI;
})();

/*
 * CREATE SORT CONTROL BJECT
 */
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
	
	me.lib.SortControl.prototype.cleanSortControl = function() {
		this.direction = undefined;
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

/*
 * CREATE OBJECT DIV
 */

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



/*
 * CREATE OBJECT TD ELEMENT
 */
(function() {
	
	me.lib.TableTd = function(htmlStr) {
		if(isSet(htmlStr)) {
			$UI.call(this,'td', htmlStr);
		} else {
			$UI.call(this, 'td');
		}
	};
	
	me.lib.TableTd.displayName = 'td';
	
	me.lib.TableTd.prototype = Object.create($UI.prototype);
	
	window.me.lib.TableTd = me.lib.TableTd;
})();
/*
	set value for td
*/
me.lib.TableTd.prototype.setValue = function(value) {
	this.htmlElement.innerHTML = value;
};
/*
	get value of this td
*/
me.lib.TableTd.prototype.getValue = function() {
	return $(this.htmlElement).html();
};

/*
 * begin load table
*/

if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		table : function(config) {
			var table = new me.lib.table(config);
			table.transform(this[0]);

			$(this).data('table', table);

			return table;
		}
	});
}

/*
 * CREATE OBJECT TH ELEMENT
 */
(function() {
	
	me.lib.TableTh = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'th', node);
		} else {
			$UI.call(this, 'th');
		}
		this.applyCss('dapps-table-header');
	};
	
	me.lib.TableTh.displayName = 'th';
	
	me.lib.TableTh.prototype = Object.create($UI.prototype);
	
	window.me.lib.TableTh = me.lib.TableTh;
})();

/*
 * CREATE OBJECT TR ELEMENT
 */
(function() {
	me.lib.TableRow = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'tr', node);
		} else {
			$UI.call(this, 'tr');
		}
		this.applyCss('dapps-table-row-hoverable');
	};
	
	me.lib.TableRow.displayName = 'tr';
	
	me.lib.TableRow.prototype = Object.create($UI.prototype);
	
	me.lib.TableRow.prototype.hide = function() {
		$(this.htmlElement).hide();
	};
	me.lib.TableRow.prototype.show = function() {
		$(this.htmlElement).show();
	};
	
	window.me.lib.TableRow = me.lib.TableRow;
})();


/*
 * CREATE OBJECT HEADER TABLE
 */
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


/*
 * CREATE OBJECT BODY TABLE
 */
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

/*
 * CREATE OBJECT FOOTER
 */
(function() {
	me.lib.TableFooter = function(currentPage, totalPage, totalColumn) {
		
		this.toFirstPageButton = new me.lib.Div();
		this.toLastPageButton = new me.lib.Div();
		this.toNextPageButton = new me.lib.Div();
		this.toPrePageButton = new me.lib.Div();
		this.valuePaging = new me.lib.Div();
		this.page = isSet(currentPage) ? currentPage : 0;
		this.totalColumn = totalColumn;
		this.totalPage = totalPage;
		
		$UI.call(this, 'tfoot');
	};
	
	me.lib.TableFooter.displayName = 'tfoot';
	
	me.lib.TableFooter.prototype = Object.create($UI.prototype);
	
	/*
	 * set visibility 
	 */
	me.lib.TableFooter.prototype.setVisibility = function() {
		if(this.page > 1) {
			$(this.toFirstPageButton.htmlElement).show();
			$(this.toPrePageButton.htmlElement).show();
		} else {
			$(this.toFirstPageButton.htmlElement).hide();
			$(this.toPrePageButton.htmlElement).hide();
		}
		if(this.page < this.totalPage) {
			$(this.toLastPageButton.htmlElement).show();
			$(this.toNextPageButton.htmlElement).show();
		} else {
			$(this.toLastPageButton.htmlElement).hide();
			$(this.toNextPageButton.htmlElement).hide();
		}
	};
	/*
	 * first load paging 
	 * get data of me.lib.TableFooter to load paging
	*/
	
	me.lib.TableFooter.prototype.loadPage = function() {
		var _this = this;
		var tr = new me.lib.TableRow();
		var td = new me.lib.TableTd();
		this.valuePaging.applyCss('dapps-table-paging-text');
		$(td.htmlElement).attr('colspan', this.totalColumn);
		this.toFirstPageButton.applyCss('dapps-table-first-page');
		this.toLastPageButton.applyCss('dapps-table-last-page');
		this.toNextPageButton.applyCss('dapps-table-next-page');
		this.toPrePageButton.applyCss('dapps-table-previous-page');
		this.toFirstPageButton.setValue('<<');
		this.toLastPageButton.setValue('>>');
		this.toNextPageButton.setValue('>');
		this.toPrePageButton.setValue('<');
		td.applyCss('clearfix dapps-table-footer');
		td.appendChild(this.toFirstPageButton);
		td.appendChild(this.toLastPageButton);
		td.appendChild(this.toNextPageButton);
		td.appendChild(this.toPrePageButton);
		td.appendChild(this.valuePaging);
		tr.appendChild(td);
		
		$(this.toFirstPageButton.htmlElement).click(function(){
			_this.toFirstPage();
		});
		
		$(this.toPrePageButton.htmlElement).click(function() {
			_this.toPrePage();
		});
		
		$(this.toLastPageButton.htmlElement).click( function() {
			_this.toLastPage();
		});
		
		$(this.toNextPageButton.htmlElement).click(function() {
			_this.toNextPage();
		});
		
		this.appendChild(tr);
		this.resetText();
	};
	
	
	/*
	 * get action when click next page button
	*/
	
	me.lib.TableFooter.prototype.toNextPage = function() {
		if(this.page + 1 <= this.totalPage) {
			this.page ++;
		}
		this.resetText();
		this.firePagingEvent();
	};
	
	/*
	 * get action when click next pre button
	*/
	me.lib.TableFooter.prototype.toPrePage = function() {
		if(this.page > 1) {
			this.page --;
		}
		this.resetText();
		this.firePagingEvent();
	};
	
	/*
	 * get action when click last page button
	*/
	me.lib.TableFooter.prototype.toLastPage = function() {
		this.page = this.totalPage;
		this.resetText();
		this.firePagingEvent();
	};
	
	/*
	 * get action when click first page button
	*/
	me.lib.TableFooter.prototype.toFirstPage = function() {
		this.page = 1;
		this.resetText();
		this.firePagingEvent();
	};
	
	/*
	 * reset text of paging
	 * display paging text [ currentPage/totalPage ]
	*/
	me.lib.TableFooter.prototype.resetText = function() {
		this.setVisibility();
		$(this.valuePaging.htmlElement).text(this.page + '/' + this.totalPage);
	};
	
	/*
	 * fire action to parent when click paging
	*/
	me.lib.TableFooter.prototype.firePagingEvent = function() {
		if(isFunction(this.onpaging)) {
			this.onpaging(this);
		}
	};
	window.me.lib.TableFooter = me.lib.TableFooter;
})();


/*
 * CREATE OBJECT TABLE
 */
(function() {
	me.lib.table = function(config) {
		//CONFIG TABLE
		this.config = config == null ? new Object : config;

		this.default_config = {
			width : 100,
			sortable : false,
			sort_data : undefined,
			row_selected : undefined,
			paging : false,
			use_hashes : false,
			scan_url_response_time : 500,
			scrollable : false,
			scroll_size : 10,
			no_data_message : 'No data',
			isResetSortAfterSearch : false,
			post_load : undefined,
			pre_load : undefined,
			default_sort : undefined,
			method : 'get',
			flow_id : undefined
		};
		
		for (key in this.default_config) {
			if (isUnset(this.config[key])) {
				this.config[key] = this.default_config[key];
			}
		}
		this.is_loaded = false;
		this.id = new Date().getTime();
		$UI.call(this, 'table');
	};
	me.lib.table.displayName = 'table';
	me.lib.table.prototype = Object.create($UI.prototype);
	
	/*
	 * transform data for table
	 */
	me.lib.table.prototype.transform = function(table) {
		this.table = table;
		this.firstLoad();
		
		table.appendChild(this.htmlElement);
	};

	
	
	/*
	 * call ajax when first load or click sort, change page
	 */
	me.lib.table.prototype.callAjax = function(isFirstLoad) {
		
		var _this = this;
		var url = _this.hashUrl(true);
		
		if(isSet(isFirstLoad)) {
			url += '&firstload=true';
		}
		
		if(_this.config.method.toLowerCase() == 'post') {
			if(isSet($('#' + _this.config.flow_id).val())) {
				url += '&flow_id=' + $('#' + _this.config.flow_id).val();
			}
		}
		
		$.ajax({
			url : _this.config.url + url,
			type : 'get',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			success : function(response) {
				if(isFirstLoad) {
					var thead = $(_this.table).find('thead')[0];
					_this.removeHeader();
					_this.createHeader(thead);
				}
				_this.tableProcess($.trim(response));
			},
			error : function(e) {
				if (isFunction(_this.config.post_load)) {
					_this.config.post_load(_this, e);
				}
			}
		});
		
		if(this.config.use_hashes) {
			var url = _this.hashUrl();
			window.location.hash = url;
		}
		
		
	};
	/*
	 * apply body when table have action
	 */
	me.lib.table.prototype.tableProcess = function(responseText) {
		this.createBody(responseText);
		this.createFooter(this.page, this.totalPage);
	};
	
	/*
	 * first load table
	 * load data in when the first load table
	 */
	
	me.lib.table.prototype.firstLoad = function() {
		$(this.htmlElement).attr('me-dapps-table', this.id);
		this.cleanFormSearch();
		this.applyCss('dapps-table-main');
		$(this.htmlElement).attr('style' , 'width : ' + this.config.width.toString() + '%');
		
		if(this.config.use_hashes) {
			this.encodeUrl();
		}
		
		this.callAjax(true);
	};
	
	/*
	 * append header to table
	 */
	me.lib.table.prototype.createHeader = function(headerHTML) {
		var head = new me.lib.TableHeader();
		this.appendChild(head);
		this.head = head;
		head.createHeaderFromHTML(headerHTML);
		
		head.onsorting = function(head) {
			head.$parent.sort = head.sort;
			head.$parent.direction = head.direction;
			head.$parent.callAjax();
		};
	};
	/*
	 * remove header when first load, change header to table controls
	 */
	me.lib.table.prototype.removeHeader = function() {
		$(this.table).find('thead').remove();
	};
	/*
	 * append body to tableremove body
	 */
	
	me.lib.table.prototype.createBody = function(response) {
		this.removeBody();
		var body = new me.lib.TableBody();
		this.appendChild(body);
		body.createBodyFromHTML(response, this);
		body.restoreDataSearch(response, this.config.store_data);
		this.body = body;
		this.localChildrenNode = this.body.children;
	};
	
	
	/*
	 * remove body
	 */
	me.lib.table.prototype.removeBody = function() {
		$(this.htmlElement).find('tbody').remove();
	};
	
	/*
	 * append footer to table
	 */
	
	me.lib.table.prototype.createFooter = function(currentPage, totalPage) {
		currentPage = isUnset(currentPage) ? 1 : currentPage;
		var _this = this;
		if(_this.config.paging) {
			_this.removeFooter();
			var tfoot = new me.lib.TableFooter(currentPage, totalPage, _this.head.count_column);
			_this.appendChild(tfoot);
			_this.footer = tfoot;
			tfoot.loadPage();
			/*
				* get action click paging of footer
				* paremer : 1 footer
			*/
			tfoot.onpaging = function(tfoot) {
				tfoot.$parent.page = tfoot.page;
				_this.callAjax();
			};
		}
	};
	
	/*
	 * remove footer
	 */
	me.lib.table.prototype.removeFooter = function() {
		$(this.htmlElement).find('tfoot').remove();
	};
	
	/*
	 * encode url from hash url
	 */
	
	me.lib.table.prototype.encodeUrl = function() {
		var _this = this;
		var currentUrl = window.location.hash;
		var url = currentUrl.split('#');
		
		if(url.length == 2) {
			var params = url[1].split('&');
			_this.queries = new Array();
			
			for(var index = 0; index < params.length ; index++) {
				var param = params[index].split('=');
				var query_name = param[0];
				var query_value = param[1];
				
				if(query_name == 'page') {
					_this.page = parseInt(query_value);
				}
				
				if(query_name == 'sort') {
					_this.config.default_sort = query_value;
					_this.sort = query_value;
				}
				
				if(query_name == 'direction') {
					_this.direction = query_value;
				}
				
				if(query_name != 'page' && query_name != 'sort' && query_name != 'direction') {
					if(isUnset(query_name) || query_name != '') {
						_this.queries.push({'query_name' : query_name, 'query_value' : query_value});
					}
					
				}
			}
		}
	};
	
	/*
	 * parset data to url
	 * param : is bool, check if generate url for ajax (true) or to hash url (false, undefined
	 */
	
	me.lib.table.prototype.hashUrl = function(isHashUrl) {
		var _this = this;
		var url = '';
		
		if(this.config.paging && isUnset(isHashUrl)) {
			if(isUnset(_this.page)) {
				url += 'page=1';
			} else {
				url += 'page=' + _this.page.toString();
			}
		} else if(this.config.paging) {
			if(isUnset(_this.page)) {
				url += '&page=0';
			} else {
				var _page = parseInt(_this.page) - 1;
				url += '&page=' + _page.toString();
			}
		}
		
		if(isSet(_this.sort) && isSet(_this.direction)) {
			url += '&sort=' + _this.sort;
			url += '&direction=' + _this.direction;
		} else {
			if(isSet(_this.config.default_sort)) {
				url += '&sort=' + _this.config.default_sort + '&direction=desc';
			}
		}
		if(isSet(_this.queries)) {
			$.each(_this.queries, function(index) {
				url += '&' + _this.queries[index]['query_name'] + '=' + _this.queries[index]['query_value'];
			});
		}
		return url;
	};
	
	
	
	/*
	* search data when user have search action
	* param : data user input, is Array
	* params format : [{
			query_name : '....',
			query_value : ....
		},...]
	 */
	me.lib.table.prototype.search = function(searchParams) {
		this.page = 1;
		this.sort = this.config.default_sort;
		this.direction = 'desc';
		if(this.config.paging) {
			this.head.reloadSortControls();
			this.setDataSearch(searchParams);
			this.callAjax();
		} else {
			this.searchLocal(searchParams);
		}
		
	};
	
	/*
	 * set data for table when user call table search function
	 */
	
	me.lib.table.prototype.setDataSearch = function(searchParams) {
		if(isArray(searchParams)) {
			this.queries = new Array();
			
			for(var index = 0; index < searchParams.length; index++ ) {
				this.queries.push({'query_name' : searchParams[index]['query_name'], 'query_value' : searchParams[index]['query_value']});
			}
		}
	};
	
	/*
		* search data in table  case not paging
		* param : data user input, is Array 
		* params format : [{
				query_name : '....',
				query_value : ....
			},...]
	*/
	me.lib.table.prototype.searchLocal = function(searchParams) {
		if(isArray(searchParams)) {
			var _this = this;
			for(var node in _this.localChildrenNode) {
				
				for(var data_bind in searchParams) {
					var indexColumn = searchParams[data_bind]['indexColumn'];
					var type = searchParams[data_bind]['comparison'];
					var compareValue = searchParams[data_bind]['query_value'].replace(/－/g,'ー');
					
					if(indexColumn >= 1 && indexColumn <=  _this.localChildrenNode[node].children.length) {
						var baseValue = _this.localChildrenNode[node].children[indexColumn-1].getValue().replace(/－/g,'ー');
						if(_this.compare(compareValue, baseValue, type)) {
							_this.localChildrenNode[node].show();
							break;
						} else {
							_this.localChildrenNode[node].hide();
						}
					}
				}
			}
		}
		
		
	};
	me.lib.table.prototype.setDefaultSort = function(default_sort) {
		this.config.default_sort = default_sort;
	};
	/*
	 * compare string 
	 * return true if the base search value match comparison with compare value else return false
	*/
	
	me.lib.table.prototype.compare = function(compareValue, baseValue, comparison) {
		var result = false;
		switch (comparison) {
			case 'ilike':
				result = baseValue.toLowerCase().indexOf(compareValue.toLowerCase()) > -1;
				break;
			case 'like':
				result = baseValue.indexOf(compareValue) > -1;
				break;
			case '>':
				result = baseValue > compareValue;
				break;
			case '>=':
				result = baseValue >= compareValue;
				break;
			case '<=':
				result = baseValue <= compareValue;
				break;
			case '<':
				result = baseValue < compareValue;
				break;
			case '!=':
				result = baseValue != compareValue;
				break;
			case '=':
			default:
				result = baseValue == compareValue;
				break;
		}
		return result;
	};
	/*
	 * set default text for search textbox, select box....
	 * data format : [{'id' : elementId,
	 * 					'query_name' : query name}]
	 * */
	me.lib.table.prototype.setDefaultText = function(eBindingData) {
		if(isArray(eBindingData) && isSet(this.queries)) {
			for(var index = 0; index < eBindingData.length; index++) {
				var elementId = '#' + eBindingData[index]['id'];
				
				for(var i = 0; i < this.queries.length; i++) {
					if(this.queries[i]['query_name'] == eBindingData[index]['query_name']) {
						$(elementId).val(this.queries[i]['query_value']);
						break;
					}
				}
			}
		}
	};
	/*
	 * clean all text store in form
	 */
	me.lib.table.prototype.cleanFormSearch = function() {
		if(isArray(this.config.store_data)) {
			for(var index = 0; index < this.config.store_data.length; index++) {
				$('#' + this.config.store_data[index]['id']).val('');
			}
		}
	};
	window.me.lib.table = me.lib.table;
})();
