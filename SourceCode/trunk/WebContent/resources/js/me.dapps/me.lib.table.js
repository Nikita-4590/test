/*
	begin load table
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
// CREATE OBJECT TABLE
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
			http_request_id : undefined
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
			if(isSet($('#' + _this.config.http_request_id).val())) {
				url += '&http_request_id=' + $('#' + _this.config.http_request_id).val(); 
			}
		}
		$.ajax({
			url : _this.config.url + url,
			type : 'get',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			success : function(response) {
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
		$(this.htmlElement).attr('style' , 'width : ' + this.config.width + '%');
		if(this.config.use_hashes) {
			this.encodeUrl();
		} 
		
		var thead = $(this.table).find('thead')[0];
		this.removeHeader();
		this.createHeader(thead);
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
