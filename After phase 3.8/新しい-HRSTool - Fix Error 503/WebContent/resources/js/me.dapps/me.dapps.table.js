$(document).ready(function () {
	$('input').placeholder();
});

/*
 * 
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
		if (isSet(node) && isSet(node.nodeType)) {
			var myLibXtag = me.dapps;
			var xtagFunction = undefined;
			for ( var key in myLibXtag) {
				if (tagName == myLibXtag[key].displayName) {
					xtagFunction = key;
					break;
				}
			}

			var meUI = new window['me']['dapps'][xtagFunction](this.tagName);
			meUI.applyAllAttr(node);

			var checkXtagCell = window['me']['dapps'][xtagFunction].displayName;
			if (isSet(checkXtagCell) && (checkXtagCell == 'td' || checkXtagCell == 'th')) {
				var parseNode = $(node).html();

				$(meUI.htmlElement).html(parseNode);
			} else {
				var checkHasChild = false;

				for ( var key in myLibXtag) {
					var xtag = myLibXtag[key].displayName;

					if (isSet(xtag)) {
						var children = $(node).find(xtag);

						for ( var index = 0; index < children.length; index++) {
							var childrenUi = $UI.createDomFromHTML(xtag,
									children[index]);
							meUI.appendChild(childrenUi);
							checkHasChild = true;
						}
					}
				}

				if (!checkHasChild) {
					$(meUI.htmlElement).text($(node).text());
				}
			}

			return meUI;
		}

	};
	/*
	 * re-append child from list children
	 */
	$UI.prototype.appendChildFromChildren = function() {
		var _this = this;
		$(this.htmlElement).empty();
		for(var i = 0; i< this.children.length; i++) {
			this.htmlElement.appendChild(this.children[i].htmlElement);
			if (isFunction(_this.$parent.config.row_selected)) {
				
				this.applyCss('dapps-table-row-hoverable');
				$(this.children[i].htmlElement).click(function() {
					_this.$parent.config.row_selected(this);
				});
			}
			
		}
	};

	
	/*
	 * append child
	 */
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
		
		var element = $(this.htmlElement);
		element.addClass(cssClass);
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
	me.dapps.SortControl = function(defaultSort, thisSortValue) {
		this.defaultSort = defaultSort;		
		this.sort = thisSortValue;
		$UI.call(this, 'div');
		 this.applyCss('dapps-table-header-sort-control');
	};	
	me.dapps.SortControl.displayName = 'div';
	me.dapps.SortControl.prototype = $UI.prototype;
	
	/*
		set value for sort controls if it is columns have sort default
	*/
	me.dapps.SortControl.prototype.applySortDefault = function() {
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
	me.dapps.SortControl.prototype.applySort = function(sortValue) {
		
		if(sortValue == this.sort) {
			this.direction = this.direction == 'desc' ? 'asc' : 'desc';
		} else if(isSet(this.direction)) {
			this.direction = this.direction == 'asc' ? 'desc' : 'asc';
		} else {
			this.direction = 'desc';
		}
		
		$(this.htmlElement).removeClass('dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-desc').addClass('dapps-table-sort-control-' + this.direction);
	};
	
	me.dapps.SortControl.prototype.cleanSortControl = function() {
		this.direction = undefined;
	};
	
	me.dapps.SortControl.prototype.resetSortControl = function(sortValue) {
		if(this.sort != sortValue) {
			this.direction = undefined;
		}
	};
	/*
	 * reload sort when search
	 * parameter : sort_default after search
	 */
	me.dapps.SortControl.prototype.reloadSort = function(sortValue, direction) {
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
	window.me.dapps.SortControl = me.dapps.SortControl;
})();

/*
 * CREATE OBJECT DIV
 */

(function() {
	me.dapps.Div = function(defaultSort) {
		$UI.call(this, 'div');
	};
	me.dapps.Div.prototype =$UI.prototype;
	
	
	me.dapps.Div.prototype.setValue = function(value) {
		$(this.htmlElement).html(value);
	};
	
	window.me.dapps.Div = me.dapps.Div;
})();



/*
 * CREATE OBJECT TD ELEMENT
 */
(function() {
	
	me.dapps.TableTd = function(htmlStr) {
		if(isSet(htmlStr)) {
			$UI.call(this,'td', htmlStr);
		} else {
			$UI.call(this, 'td');
		}
	};
	
	me.dapps.TableTd.displayName = 'td';
	
	me.dapps.TableTd.prototype = $UI.prototype;;
	
	window.me.dapps.TableTd = me.dapps.TableTd;
})();
/*
	set value for td
*/
me.dapps.TableTd.prototype.setValue = function(value) {
	$(this.htmlElement).append(value);
};
/*
	get value of this td
*/
me.dapps.TableTd.prototype.getValue = function() {
	return $(this.htmlElement).html();
};

/*
 * begin load table
*/

if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		table : function(config) {
			var table = new me.dapps.table(config);
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
	
	me.dapps.TableTh = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'th', node);
		} else {
			$UI.call(this, 'th');
		}
		this.applyCss('dapps-table-header');
	};
	
	me.dapps.TableTh.displayName = 'th';
	
	me.dapps.TableTh.prototype = $UI.prototype;;
	
	window.me.dapps.TableTh = me.dapps.TableTh;
})();

/*
 * CREATE OBJECT TR ELEMENT
 */
(function() {
	me.dapps.TableRow = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'tr', node);
		} else {
			$UI.call(this, 'tr');
		}
		this.applyCss('dapps-table-row-hoverable');
	};
	
	me.dapps.TableRow.displayName = 'tr';
	
	me.dapps.TableRow.prototype = $UI.prototype;;
	
	me.dapps.TableRow.prototype.hide = function() {
		$(this.htmlElement).hide();
	};
	me.dapps.TableRow.prototype.show = function() {
		$(this.htmlElement).show();
	};
	
	window.me.dapps.TableRow = me.dapps.TableRow;
})();


/*
 * CREATE OBJECT HEADER TABLE
 */
(function() {
	me.dapps.TableHeader = function() {
		$UI.call(this, 'thead');
		
	};
	
	me.dapps.TableHeader.displayName = 'thead';
	
	me.dapps.TableHeader.prototype = $UI.prototype;;
	
	/*
	 * generated header from html String
	 * parameter is String and have construct as : '<thead><tr><th>.....</th></tr></thead>'
	 */
	me.dapps.TableHeader.prototype.createHeaderFromHTML = function(thead) {
		var _this = this;
		var rows = $(thead).find('tr');
		var table_config = _this.$parent.config;
		
		for(var index = 0; index < rows.length ; index++) {
			
			var tr = new me.dapps.TableRow();
			_this.appendChild(tr);
			var cells = $(rows[index]).find('th');
			_this.count_column = cells.length;
			
			for(var indexCells = 0 ; indexCells < cells.length; indexCells++) {
				
				var cssClass = $(cells[indexCells]).attr('class');
				var th = new me.dapps.TableTh();
				th.applyCss(cssClass);
				tr.appendChild(th);
				var div = new me.dapps.Div();
				th.appendChild(div);
				div.applyCss('dapps-table-header-text');
				div.setValue($(cells[indexCells]).html());
				
				if(table_config.sortable && isSet(table_config.sort_data) && table_config.sort_data.length == cells.length && (table_config.sort_data[indexCells]) && table_config.sort_data[indexCells].length != 0) {
					
					var isDefault = false;
					if(table_config.sort_data[indexCells] == table_config.default_sort) {
						isDefault = true;
						_this.sort = table_config.sort_data[indexCells];
					}
					
					var divSort = new me.dapps.SortControl(isDefault, table_config.sort_data[indexCells]);
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
	
	me.dapps.TableHeader.prototype.reloadSortActionClick = function(thElement, sortValue) {
		
		for(var index = 0; index < thElement.children.length; index ++) {
			if(isFunction(thElement.children[index].resetSortControl)) {
				thElement.children[index].resetSortControl(sortValue);
			}
		}
	};
	/*
	 * reload class sort of reload sort controls in the header
	 */
	
	me.dapps.TableHeader.prototype.reloadSortControls = function() {
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
	
	me.dapps.TableHeader.prototype.reloadSortAfterReloadBody = function() {
		var _this = this;
		_this.removeSortCss();
		for(var index = 0; index < _this.children.length; index++) {
			var rowChildren = _this.children[index];
			
			for(var indexRow = 0; indexRow < rowChildren.children.length; indexRow++) {
				var divChildren = rowChildren.children[indexRow].children;
				for(var divIndex = 0; divIndex < divChildren.length; divIndex++) {
					if(isFunction(divChildren[divIndex].reloadSort)) {
//						divChildren[divIndex].reloadSort(isSet(_this.$parent.sort) ? _this.$parent.sort : _this.$parent.config.default_sort, isUnset(_this.$parent.direction) ? _this.$parent.direction : 'desc');
						divChildren[divIndex].reloadSort(_this.$parent.sort, _this.$parent.direction);
					}
				}
			}
		}
	};
	
	/*
	 * remove sort all class sort of sort controls
	*/
	
	
	me.dapps.TableHeader.prototype.removeSortCss = function() {
		$(this.htmlElement).find('.dapps-table-sort-control-desc').removeClass('dapps-table-sort-control-desc');
		$(this.htmlElement).find('.dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-asc');
	};
	
	window.me.dapps.TableHeader = me.dapps.TableHeader;
})();

/*
 * CREATE OBJECT BODY TABLE
 */
(function() {
	me.dapps.TableBody = function(node) {
		if(isSet(node)) {
			$UI.call(this, 'tbody', node);
		} else {
			$UI.call(this, 'tbody');
		}
		
	};
	
	me.dapps.TableBody.displayName = 'tbody';
	me.dapps.TableBody.prototype = $UI.prototype;;
	/*
	 * find all attr of body(current page, total page, data search 
	 */
	me.dapps.TableBody.prototype.restoreDataSearch = function(tbody, stored_data_search) {
		var _this = this;
		
		_this.$parent.totalPage = isSet($(tbody).attr('total-page')) ? $(tbody).attr('total-page') : 1;
		_this.$parent.head.reloadSortAfterReloadBody();
	};
	/*
		create tbody from html string
		data input parameter :  +	String as '<tbody><tr>....</tr><tr>....</tr></tbody>'
	*/

	me.dapps.TableBody.prototype.createBodyFromHTML = function(tbodyHTML) {
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
		data input parameter : 	Array[me.dapps.table_row]
	*/
	me.dapps.TableBody.prototype.createBodyFromNode = function(tbodyNodes) {
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
	
	me.dapps.TableBody.prototype.createBodyNoData = function() {
		var _this = this;
		var noDataCell = new me.dapps.TableTd();
		noDataCell.applyCss('dapps-table-no-data');
		noDataCell.setValue(_this.$parent.config.no_data_message);
		var noDataRow = new me.dapps.TableRow();
		
		noDataRow.applyCss('dapps-table-no-data-row');
		noDataRow.appendChild(noDataCell);
		_this.appendChild(noDataRow);
		noDataCell.applyAttr('colspan', _this.$parent.head.count_column);
		
	};
	
	window.me.dapps.TableBody = me.dapps.TableBody;
})();

/*
 * CREATE OBJECT FOOTER
 */
(function() {
	me.dapps.TableFooter = function(currentPage, totalPage, totalColumn) {
		
		this.toFirstPageButton = new me.dapps.Div();
		this.toLastPageButton = new me.dapps.Div();
		this.toNextPageButton = new me.dapps.Div();
		this.toPrePageButton = new me.dapps.Div();
		this.valuePaging = new me.dapps.Div();
		this.page = isSet(currentPage) ? currentPage : 0;
		this.totalColumn = totalColumn;
		this.totalPage = totalPage;
		
		$UI.call(this, 'tfoot');
	};
	
	me.dapps.TableFooter.displayName = 'tfoot';
	
	me.dapps.TableFooter.prototype = $UI.prototype;;
	
	/*
	 * set visibility 
	 */
	me.dapps.TableFooter.prototype.setVisibility = function() {
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
	 * get data of me.dapps.TableFooter to load paging
	*/
	
	me.dapps.TableFooter.prototype.loadPage = function() {
		var _this = this;
		var tr = new me.dapps.TableRow();
		var td = new me.dapps.TableTd();
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
			_this.redirectPage(4);
		});
		
		$(this.toPrePageButton.htmlElement).click(function() {
			_this.redirectPage(3);
		});
		
		$(this.toLastPageButton.htmlElement).click( function() {
			_this.redirectPage(2);
		});
		
		$(this.toNextPageButton.htmlElement).click(function() {
			_this.redirectPage(1);
		});
		
		this.appendChild(tr);
		this.resetText();
	};
	
	/*
	 * common action click next,pre,last,first page
	 */
	me.dapps.TableFooter.prototype.redirectPage = function(actionCode) {
		var _this = this;
		switch(actionCode) {
			case 1 :
				if(_this.page + 1 <= _this.totalPage) {
					_this.page ++;
				}
				break;
			case 2 :
				_this.page = _this.totalPage;
				break;
			case 3 :
				if(_this.page > 1) {
					_this.page --;
				}
				break;
			case 4 : 
				_this.page = 1;
				break;
		}
		this.resetText();
		this.firePagingEvent();
	};
	/*
	 * reset text of paging
	 * display paging text [ currentPage/totalPage ]
	*/
	me.dapps.TableFooter.prototype.resetText = function() {
		this.setVisibility();
		$(this.valuePaging.htmlElement).text(this.page + '/' + this.totalPage);
	};
	
	/*
	 * fire action to parent when click paging
	*/
	me.dapps.TableFooter.prototype.firePagingEvent = function() {
		if(isFunction(this.onpaging)) {
			this.onpaging(this);
		}
	};
	window.me.dapps.TableFooter = me.dapps.TableFooter;
})();


/*
 * CREATE OBJECT TABLE
 */
(function() {
	me.dapps.table = function(config) {
		//CONFIG TABLE
		this.config = config == null ? new Object : config;

		this.default_config = {
			width : 100,
			sortable : false,
			sort_data : undefined,
			row_selected : undefined,
			is_restore_data : false,
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
			control_reload_page : false
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
	me.dapps.table.displayName = 'table';
	me.dapps.table.prototype = $UI.prototype;;
	
	/*
	 * transform data for table
	 */
	me.dapps.table.prototype.transform = function(table) {
		var _this = this;
		this.table = table;
		if(this.config.control_reload_page) {
			document.body.addEventListener('keydown', function (e) {
				if(!isReferrer()) {
					
					var keycode = undefined;
					if(window.event) {
						keycode = window.event.keyCode;
					} else {
						keycode = e.which;
					}
					var userAgent = window.navigator.userAgent;
					//check if browser is IE
					var msie = userAgent.indexOf("MSIE");
					if(msie > 0) {
						if((keycode == 116 || (window.event.ctrlKey && keycode == 82))) {
							e.preventDefault();
							_this.callAjax(false);
						}
					} else {
						if (keycode == 116 ||(e.ctrlKey && keycode == 82)) {
			                e.preventDefault();
			                _this.callAjax(false);
			            }
					}
				}
			});
		}
		this.firstLoad();
		
		table.appendChild(this.htmlElement);
	};

	
	
	/*
	 * call ajax when first load or click sort, change page
	 */
	me.dapps.table.prototype.callAjax = function(isFirstLoad) {
		var _this = this;
		
		if(isSet(this.config.method) && this.config.method.toLowerCase() == 'post') {
			_this.ajaxPost(isFirstLoad);
		} else {
			_this.ajaxGet( isFirstLoad);
		}
		
		if(this.config.use_hashes) {
			var url = _this.hashUrl();
			window.location.hash = url;
		}
		
	};
	/*
	 * call ajax with method get
	 */
	
	me.dapps.table.prototype.ajaxGet = function(isFirstLoad) {
		
		var _this = this;
		if(_this.config.is_restore_data) {
			_this.setSessionStorage();
		}
		
		var url = '&' + 'rand=' + new Date().getTime() + _this.hashUrl(true);
		if(_this.entity != null) {
			url += '&' + _this.entity['name'] + '=' + _this.entity['id'];
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
	};
	/*
	 * call ajax with post method
	 */
	
	me.dapps.table.prototype.ajaxPost = function(isFirstLoad) {
		var data = {};
		var _this = this;
		if(_this.config.is_restore_data) {
			_this.setSessionStorage();
		}
		if(this.config.paging) {
			var currentPage = isUnset(_this.page) ? 0 : (_this.page-1).toString();
			data['page'] = currentPage;
		}
		if(isSet(_this.sort) && isSet(_this.direction)) {
			data['sort'] = _this.sort;
			data['direction'] = _this.direction;
		} else {
			
			data['sort'] = _this.config.default_sort;
			data['direction'] = 'desc';
		}
		
		if(isSet(_this.entity)) {
			var entity = _this.entity;
			data[entity['name']] = entity['id'];
		}

		if(isSet(_this.queries)) {
			$.each(_this.queries, function(index) {
				data[_this.queries[index]['query_name']] = _this.queries[index]['query_value'];
			});
		}
		$.ajax({
			  type: "POST",
			  url: _this.config.url,
			  data: data,
			  contentType : "application/x-www-form-urlencoded; charset=utf-8",
			  success : function(response) {
				  	var trimResponse = $.trim(response);
					if(isSet(isFirstLoad) && isFirstLoad) {
						var thead = $(_this.table).find('thead')[0];
						_this.removeHeader();
						_this.createHeader(thead);
					}
					
					if(isFunction(_this.config.showMessage) && isSet($(trimResponse).attr('message'))) {
						_this.config.showMessage($(trimResponse).attr('message'));
					}
					
					_this.tableProcess(trimResponse);
				},
				error : function(e) {
					if (isFunction(_this.config.post_load)) {
						_this.config.post_load(_this, e);
					}
				}
			});
	};
	
	/*
	 * apply body when table have action
	 */
	me.dapps.table.prototype.tableProcess = function(responseText) {
		this.createBody(responseText);
		this.createFooter(this.page, this.totalPage);
	};
	
	/*
	 * first load table
	 * load data in when the first load table
	 */
	
	me.dapps.table.prototype.firstLoad = function() {
		$(this.htmlElement).attr('me-dapps-table', this.id);
		this.cleanFormSearch();
		this.applyCss('dapps-table-main');
		$(this.htmlElement).attr('style' , 'width : ' + this.config.width.toString() + '%');
		
		if(this.config.use_hashes) {
			this.encodeUrl();
		}
		this.beforeLoad();
		
		this.callAjax(true);
	};
	
	/*
	 * append header to table
	 */
	me.dapps.table.prototype.createHeader = function(headerHTML) {
		var head = new me.dapps.TableHeader();
		this.appendChild(head);
		this.head = head;
		head.createHeaderFromHTML(headerHTML);
		
		head.onsorting = function(head) {
			head.$parent.sort = head.sort;
			head.$parent.direction = head.direction;
			
			if(isSet(head.$parent.config.paging) && head.$parent.config.paging) {
				head.$parent.callAjax();
			} else {
				head.$parent.sortLocal();
			}

		};
	};
	/*
	 * remove header when first load, change header to table controls
	 */
	me.dapps.table.prototype.removeHeader = function() {
		$(this.table).find('thead').remove();
	};
	/*
	 * append body to tableremove body
	 */
	
	me.dapps.table.prototype.createBody = function(response) {
		this.removeBody();
		var body = new me.dapps.TableBody();
		this.appendChild(body);
		body.createBodyFromHTML(response, this);
		body.restoreDataSearch(response, this.config.store_data);
		this.body = body;
		this.localChildrenNode = this.body.children;
	};
	
	
	/*
	 * remove body
	 */
	me.dapps.table.prototype.removeBody = function() {
		$(this.htmlElement).find('tbody').remove();
	};
	
	/*
	 * append footer to table
	 */
	
	me.dapps.table.prototype.createFooter = function(currentPage, totalPage) {
		currentPage = isUnset(currentPage) ? 1 : currentPage;
		var _this = this;
		if(_this.config.paging) {
			_this.removeFooter();
			var tfoot = new me.dapps.TableFooter(currentPage, totalPage, _this.head.count_column);
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
	me.dapps.table.prototype.removeFooter = function() {
		$(this.htmlElement).find('tfoot').remove();
	};
	
	/*
	 * encode url from hash url
	 */
	
	me.dapps.table.prototype.encodeUrl = function() {
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
	 * parse data to url
	 * param : is bool, check if generate url for ajax (true) or to hash url (false, undefined
	 */
	
	me.dapps.table.prototype.hashUrl = function(isHashUrl) {
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
				url += '&' + _this.queries[index]['query_name'] + '=' + encodeURIComponent(_this.queries[index]['query_value']);
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
	me.dapps.table.prototype.search = function(searchParams, new_sort) {
		this.page = 1;
		if(isSet(new_sort)) {
			this.setDefaultSort(new_sort);
		}
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
	 * sort local
	 */
	
	me.dapps.table.prototype.sortLocal = function() {
		var _this = this;
		var tableBody = this.body;
		if(tableBody.children.length != 0) {
			var countCol = tableBody.children[0].children.length;
			var countSort = -1;
			for(var i = 0; i< _this.config.sort_data.length; i++) {
				if(_this.sort = _this.config.sort_data[i]) {
					countSort = i;
					break;
				}
			}
			
			if(countSort != -1 && countCol >= countSort) {
				var children = undefined;
				
				children = _this.createChildrenSortLocal(tableBody.children, countSort, _this.direction);
				tableBody.children = children;
				_this.body = tableBody;
				_this.body.appendChildFromChildren();
			}
		
		}
		
	};

	
	me.dapps.table.prototype.createChildrenSortLocal = function(children, countColSort, direction) {
		for(var i = 0; i< children.length-1; i++) {
			for(var j = 0; j<children.length - i - 1; j++) {
				var compareVal1 = $(children[j].children[countColSort].htmlElement).text();
				var compareVal2 = $(children[j+1].children[countColSort].htmlElement).text();
				
				if(compareVal1 < compareVal2) {
					var swap = children[j];
					children[j] = children[j+1];
					children[j+1] = swap;
				}
			}
		}
		if(direction == 'asc') {
			return children.reverse();
		} else {
			return children;
		}
	};

	
	/*
	 * set data for table when user call table search function
	 */
	
	me.dapps.table.prototype.setDataSearch = function(searchParams) {
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
	me.dapps.table.prototype.searchLocal = function(searchParams) {
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

	
	me.dapps.table.prototype.setDefaultSort = function(default_sort) {
		this.config.default_sort = default_sort;
	};
	/*
	 * compare string 
	 * return true if the base search value match comparison with compare value else return false
	*/
	
	me.dapps.table.prototype.compare = function(compareValue, baseValue, comparison) {
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
	me.dapps.table.prototype.setDefaultText = function(eBindingData) {
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
	 * set data for sessionStorage before load table
	 */
	
	me.dapps.table.prototype.beforeLoad = function() {
		var _this = this;
		if(isReferrer() && isSet(this.config.is_restore_data) && this.config.is_restore_data && isSet(sessionStorage.$storageActionOfUser)) {
			var isBindData = isArray(_this.config.store_data);
			
			try {
				var $storageActionOfUser = JSON.parse(sessionStorage.$storageActionOfUser);
				var keys = Object.keys($storageActionOfUser);
				for(var index = 0; index < keys.length; index++) {
					
					if(keys[index] == 'page') {
						_this.page = $storageActionOfUser['page'];
					} else if(keys[index] == 'sort') {
						_this.sort = $storageActionOfUser['sort'];
					} else if(keys[index] == 'direction') {
						_this.direction = $storageActionOfUser['direction'];
					} else if(keys[index] == 'entity') {
						_this.entity = $storageActionOfUser['entity'];
					} else {
						if(isBindData) {
							if(isUnset(_this.queries)) {
								_this.queries = new Array();
							}
							for(var param = 0; param < _this.config.store_data.length; param ++) {
								if(_this.config.store_data[param]['query_name'] == keys[index]) {
									var value = keys[index];
									_this.queries.push({'query_name' : keys[index], 'query_value' : $storageActionOfUser[value]});
									var type = $('#' + _this.config.store_data[param]['id']).attr('type');
									if(isSet(type) && type.toLowerCase() == 'checkbox') {
										$('#' + _this.config.store_data[param]['id']).prop('checked', $storageActionOfUser[value]);
									} else {
										$('#' + _this.config.store_data[param]['id']).val($storageActionOfUser[value]);
									}
								}
							}
						}
					}
				}
			} catch(err) {
				console.log(err);
				//TODO
			}
		} else {
			_this.sort = _this.config.default_sort;
			_this.direction = 'desc';
		}
	};
	
	me.dapps.table.prototype.setSessionStorage = function() {
		var $storageActionOfUser = {};
		$storageActionOfUser['page'] = isSet(this.page) ? this.page : 1;
		$storageActionOfUser['sort'] = isSet(this.sort) ? this.sort : this.config.default_sort;
		$storageActionOfUser['direction'] = isSet(this.direction) ? this.direction : 'desc';
		
		if(isArray(this.queries)) {
			for(var index = 0; index < this.queries.length; index ++) {
				$storageActionOfUser[this.queries[index]['query_name']] = this.queries[index]['query_value'];
			}
		}
		sessionStorage.$storageActionOfUser = JSON.stringify($storageActionOfUser);
	};
	
	/*
	 * clean all text store in form
	 */
	me.dapps.table.prototype.cleanFormSearch = function() {
		if(isArray(this.config.store_data)) {
			for(var index = 0; index < this.config.store_data.length; index++) {
				$('#' + this.config.store_data[index]['id']).val('');
			}
		}
	};
	me.dapps.table.prototype.storageRowClick = function(nameStore, id) {
		var $storageActionOfUser = sessionStorage.$storageActionOfUser;
		if(isUnset($storageActionOfUser)) {
			$storageActionOfUser = {};
			$storageActionOfUser['page'] = isSet(this.page) ? this.page : 1;
			$storageActionOfUser['sort'] = isSet(this.sort) ? this.sort : this.config.default_sort;
			$storageActionOfUser['direction'] = isSet(this.direction) ? this.direction : 'desc';
		} else {
			$storageActionOfUser = JSON.parse($storageActionOfUser);
		}
		$storageActionOfUser['entity'] = {'name' : nameStore, 'id' : id};
		sessionStorage.$storageActionOfUser = JSON.stringify($storageActionOfUser);
	};
	window.me.dapps.table = me.dapps.table;
})();
