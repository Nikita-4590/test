if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		table : function(config) {
			var table = new me.dapps.table(config);

			table.transform(this);

			$(this).data('table', table);

			return table;
		}
	});
}

// sort control
me.dapps.sort_control = function(table, header, mapping) {
	this.table = table;
	this.header = $(header);
	this.mapping = mapping;
	this.createControls();
};
me.dapps.sort_control.prototype.createControls = function() {
	var thisControl = this;

	this.main = $('<div />').addClass('dapps-table-header-sort-control');
	this.header.append(this.main);

	this.main.click(function() {
		thisControl.changeDirection();
	});
};
me.dapps.sort_control.prototype.changeDirection = function() {
	if (isUnset(this.direction) || this.direction == 'asc') {
		this.direction = 'desc';
	} else {
		this.direction = 'asc';
	}

	if (isFunction(this.sorting)) {
		this.sorting(this);
	}
};
me.dapps.sort_control.prototype.removeSort = function() {
	this.main.removeClass('dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-desc');
};
me.dapps.sort_control.prototype.applySort = function() {
	this.main.removeClass('dapps-table-sort-control-asc').removeClass('dapps-table-sort-control-desc').addClass('dapps-table-sort-control-' + this.direction);
};
// table
me.dapps.table = function(config) {
	this.config = config == null ? new Object : config;

	this.default_config = {
		width : undefined,
		sortable : false,
		select_row : true,
		paging : true,
		use_hashes : true,
		scan_url_response_time : 500,
		scrollable : false,
		scroll_size : 10,
		no_data_message : 'No data',
		url_patterns : undefined,
		data_binds : undefined,
		row_clicked : undefined,
		post_load : undefined,
		pre_load : undefined,
	};

	for (key in this.default_config) {

		if (isUnset(this.config[key])) {
			this.config[key] = this.default_config[key];
		}
	}

	this.is_loaded = false;
	this.id = new Date().getTime();
};
me.dapps.table.prototype.transform = function(content) {
	var thisTable = this;
	this.first_load = true;
	this.main = $(content);

	// apply css
	this.main.attr('dapps-table-id', this.id).addClass('dapps-table-main');

	// config common style
	if (isSet(this.config.width)) {
		this.main.css({
			width : this.config.width
		});
	}

	// add a check-box column at left
	this.header_index_offset = 0;
	if (this.config.select_row) {
		$('<th />').attr('width', 30).addClass('dapps-table-select-header').insertBefore(this.main.find('thead th:first'));
		this.header_index_offset = 1;
	}

	if (this.config.scrollable) {
		this.main.find('thead tr').append($('<th />').addClass('dapps-table-scroll-header'));
	}

	this.headers = this.main.find('th').addClass('dapps-table-header');

	// make sortable header
	if (this.config.sortable && isArray(this.config.data_binds)) {
		var sortableHeader = this.headers.not('.dapps-table-select-header, .dapps-table-scroll-header');

		$.each(sortableHeader, function(index) {
			$this = $(this);

			var headerText = $this.text();
			$this.empty();
			headerText = $('<div />').text(headerText).addClass('dapps-table-header-text');

			$this.append(headerText);

			if (isUnset(thisTable.sortControls)) {
				thisTable.sortControls = new Array;
			}

			// create sortControl
			var sortControl = new me.dapps.sort_control(thisTable, $this, thisTable.config.data_binds[index].name);

			sortControl.sorting = function(sortControl) {
				sortControl.table.applySort(sortControl);
			};

			thisTable.sortControls.push(sortControl);
		});
	}

	// append body
	if (this.main.find('tbody').length == 0) {
		this.body = $('<tbody />');
		this.main.append(this.body);
	} else {
		this.body = this.main.find('tbody');
	}

	this.firstLoad();

	if (this.config.use_hashes) {
		this.scanURL();
	}
};
me.dapps.table.prototype.applySort = function(sortControl) {
	for ( var i = 0; i < this.sortControls.length; i++) {
		if (this.sortControls[i] == sortControl) {
			this.sortControls[i].applySort();
		} else {
			this.sortControls[i].removeSort();
		}
	}

	this.sorting(sortControl.mapping, sortControl.direction);
};
me.dapps.table.prototype.sorting = function(sort, sort_direction) {
	this.sort = sort;
	this.sort_direction = sort_direction;
	if (this.config.paging || (!this.config.paging && !this.is_loaded)) {
		// request new data
		this.loadData();
	} else {
		// sort localy - using global variables (...)
		me.dapps.global['table.sort'] = this.sort;
		me.dapps.global['table.sort_direction'] = this.sort_direction;
		var sortedRowData = this.rowData.sort(this.compareRow);

		for ( var i = 0; i < sortedRowData.length; i++) {
			this.body.append(sortedRowData[i].__$row);
		}

		// update hashes
		if (this.config.use_hashes) {
			var hashes = new Object;

			if (isSet(this.page)) {
				hashes['page'] = this.page;
			}

			if (isSet(this.sort)) {
				hashes['sort'] = this.sort;
			}

			if (isSet(this.sort_direction)) {
				hashes['sort_direction'] = this.sort_direction;
			}
			this.appendParametersToUrl(hashes);
		}
	}
};
me.dapps.table.prototype.compareRow = function(row1, row2) {

	var sortField = me.dapps.global['table.sort'];
	var sortDirection = me.dapps.global['table.sort_direction'];

	if (row1[sortField] < row2[sortField]) {
		return sortDirection == 'asc' ? -1 : 1;
	} else if (row1[sortField] > row2[sortField]) {
		return sortDirection == 'asc' ? 1 : -1;
	} else {
		return 0;
	}
};
me.dapps.table.prototype.dispose = function() {
	if (this.config.use_hashes) {
		if (isSet(this.scan_timer)) {
			clearInterval(this.scan_timer);
		}

		this.clearParameterFromUrl();
	}
};
me.dapps.table.prototype.scanURL = function() {
	var thisTable = this;
	thisTable.scan_timer = setInterval(function() {
		if (thisTable.hashes != (window.location.href)) {
			thisTable.loadFromHashes();
		}
	}, this.config.scan_url_response_time);
};
me.dapps.table.prototype.loadFromHashes = function() {
	var urls = (window.location + '').split('#');

	if (urls.length > 1) {
		var hashes = urls[1].split('&');
		this.page = this.config.paging ? 0 : null;
		this.sort = null;
		this.sort_direction = null;
		this.queries = null;
		for ( var i = 0; i < hashes.length; i++) {
			// get key-value pair
			var param = hashes[i].split('=');
			if (this.config.paging) {
				if (param.length > 1 && param[0] == 'page') {
					this.page = param[1];
				}
			}
			if (this.config.sortable) {
				if (param.length > 1 && param[0] == 'sort') {
					this.sort = param[1];
				}
				if (param.length > 1 && param[0] == 'sort_direction') {
					this.sort_direction = param[1];
				}
			}

			if (param.length > 1 && param[0] == 'queries') {
				try {
					this.queries = JSON.parse(param[1]);
				} catch (e) {

				}
			}
		}

		this.loadData();
	} else {
		// do nothing
	}
};
me.dapps.table.prototype.firstLoad = function() {
	var urls = (window.location + '').split('#');

	if ((!this.config.use_hashes) || (this.config.use_hashes && urls.length == 1)) {
		// no hash
		// begin default filter
		if (this.config.paging) {
			this.paging(0);
		} else {
			this.loadData();
		}
	}
};
me.dapps.table.prototype.selectRow = function(checkbox) {
	var checked = $(checkbox).prop('checked');

	var row = $(checkbox).parents('tr');

	if (checked) {
		row.addClass('dapps-table-selected-row');
	} else {
		row.removeClass('dapps-table-selected-row');
	}
};
me.dapps.table.prototype.beginLoad = function() {
	this.first_load = false;

	var offset = this.config.scrollable ? 1 : 0;

	var loadingCell = $('<td />').attr('colspan', this.headers.length - offset).addClass('dapps-table-loading-cell');

	// append
	var currentHeight = this.body.outerHeight();
	this.main.find('tbody').remove();
	this.main.append($('<tbody />').append($('<tr />').append(loadingCell)));

	var paddingTop = parseInt(loadingCell.css('padding-top'), 10);
	var paddingBottom = parseInt(loadingCell.css('padding-bottom'), 10);

	var borderWidth = parseInt(loadingCell.css('border-width'), 10);

	var diff = paddingTop + paddingBottom + borderWidth;

	if (this.config.scrollable) {
		loadingCell.css('height', this.config.scroll_size * 30 - diff);
	} else {
		if (currentHeight > 30) {
			loadingCell.css('height', currentHeight - diff);
		}
	}
};
me.dapps.table.prototype.loadData = function() {
	var thisTable = this;

	thisTable.beginLoad();

	if (typeof this.config.url_patterns == 'object') {
		// short name
		var urls = this.config.url_patterns;
		// this.clearParameterFromUrl();
		// create url for request data
		var params = new Array;
		var hashes = new Object;

		if (isSet(this.page)) {
			params.push(urls.page.replace("{page}", this.page));
			hashes['page'] = this.page;
		}

		if (isSet(this.sort)) {
			params.push(urls.sort.replace("{sort}", this.sort));
			hashes['sort'] = this.sort;
		}

		if (isSet(this.sort_direction)) {
			params.push(urls.sort_direction.replace("{sort_direction}", this.sort_direction));
			hashes['sort_direction'] = this.sort_direction;
		}

		if (isSet(this.queries)) {
			for ( var i = 0; i < this.queries.length; i++) {
				var query = this.queries[i];
				if (isSet(query.query_name) && isSet(query.query_value)) {
					var paramString = urls.query.replace("{query_name}", query.query_name);
					paramString = paramString.replace("{query_value}", encodeURIComponent(query.query_value));
					params.push(paramString);

					if (isUnset(hashes['queries'])) {
						hashes['queries'] = new Array();
					}

					hashes['queries'].push({
						query_name : query.query_name,
						query_value : query.query_value
					});
				}
			}
		}

		var requestUrl = this.buildUrl(urls.url, params);
		this.appendParametersToUrl(hashes);

		if (isFunction(this.config.pre_load)) {
			if (!this.config.pre_load(this)) {
				return;
			}
		}

		$.ajax({
			url : requestUrl,
			type : 'get',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			success : function(response) {
				thisTable.is_loaded = true;
				thisTable.displayData($.trim(response));
			},
			error : function(e) {
				thisTable.is_loaded = false;
				
				if (isFunction(thisTable.config.post_load)) {
					thisTable.config.post_load(thisTable, e);
				}
			}
		});
	}
};
me.dapps.table.prototype.displayNoData = function() {
	// this.main.empty();
	var offset = this.config.scrollable ? 1 : 0;

	var noDataCell = $('<td />').attr('colspan', this.headers.length - offset).addClass('dapps-table-no-data').text(this.config.no_data_message);
	var noDataRow = $('<tr />').append(noDataCell).addClass('dapps-table-no-data-row');
	// this.body = $('<tbody />').append();
	this.body.append(noDataRow);
	if (this.config.paging) {
		if (this.footer != null) {
			this.footer.remove();
		}
	}

	if (isFunction(this.config.post_load)) {
		this.config.post_load(this);
	}
};
me.dapps.table.prototype.displayData = function(response) {
	var thisTable = this;

	this.main.find('tbody').remove();
	this.body = $(response);

	if (response.length == 0 || !this.body.is('tbody') || this.body.children('tr').length == 0) {

		this.body = $('<tbody />');
		this.main.append(this.body);
		this.displayNoData();
		return;
	}

	// add checkbox cell

	$.each(thisTable.body.find('tr'), function() {
		if (thisTable.config.select_row) {
			var firstCell = $(this).find('td:first');

			var checkbox = $('<input />').attr('type', 'checkbox');

			$('<td />').addClass('select').append(checkbox).insertBefore(firstCell);

			// prevent user checking action causes row_selected event fired
			checkbox.click(function(e) {
				e.stopPropagation();
			});

			// bind event
			checkbox.change(function(e) {
				thisTable.selectRow(this);
				e.stopPropagation();
			});
		}

		if (isFunction(thisTable.config.row_selected)) {
			$(this).addClass('dapps-table-row-hoverable').click(function() {
				thisTable.config.row_selected(this, thisTable);
			});
		}
	});

	// apply column_styles
	if (isArray(thisTable.config.column_styles)) {
		$.each(thisTable.body.find('tr'), function() {
			var tds = $(this).find('td');
			var column_styles = thisTable.config.column_styles;
			for ( var i = 0; i < column_styles.length; i++) {
				$(tds[i + thisTable.header_index_offset]).addClass(column_styles[i]);
			}
		});
	}

	if (this.config.scrollable) {

		var scrollCell = $('<td />').attr('colspan', this.headers.length).addClass('dapps-table-scroll-cell');

		var scrollContainer = $('<div />').addClass('dapps-table-scroll-container').height(this.config.scroll_size * 30);

		scrollCell.append(scrollContainer);

		var insideTable = $('<table />').attr('cellpadding', 0).attr('cellspacing', 0).addClass('dapps-table-inside').append(thisTable.body);

		scrollContainer.append(insideTable);
		var body = $('<tbody />').append($('<tr />').append(scrollCell));

		this.main.append(body);

		// resize columns' width same as headers'
		var cellsInFirstRow = this.body.find('tr:first').find('td');
		var tableWidth = 0;
		$.each(cellsInFirstRow, function(index) {
			var paddingLeft = parseFloat($(this).css('padding-left'));
			var paddingRight = parseFloat($(this).css('padding-right'));

			var borderRightWidth = parseFloat($(this).css('border-right-width'));
			var borderLeftWidth = parseFloat($(this).css('border-left-width'));

			var borderOffset = borderRightWidth + borderLeftWidth;
			var diff = paddingLeft + paddingRight + borderOffset;
			var headerWidth = $(thisTable.headers[index]).outerWidth();
			var desireWidth = headerWidth - diff;

			$.each(thisTable.body.find('tr'), function() {
				$($(this).find('td')[index]).css({
					'width' : desireWidth,
					'max-width' : desireWidth,
					'min-width' : desireWidth
				});
			});

			// $(this).width(headerWidth - diff);
			tableWidth += headerWidth;
		});

		insideTable.width(tableWidth);

	} else {
		thisTable.main.append(thisTable.body);
	}

	// append footer
	if (thisTable.config.paging) {
		if (thisTable.footer != null) {
			thisTable.footer.remove();
		}
		thisTable.footer = $('<tfoot />');

		var footerContent = $('<td />').addClass('dapps-table-footer').addClass('clearfix').attr('colspan', thisTable.headers.length);

		// get currentPage / totalPage
		var currentPage = parseInt(thisTable.body.attr('current-page'), 10);
		var totalPage = parseInt(thisTable.body.attr('total-page'), 10);

		thisTable.createPagingButton(currentPage, totalPage, footerContent);

		footerContent.append($('<div />').addClass('dapps-table-paging-text').text((currentPage + 1) + '/' + totalPage));

		thisTable.footer.append($('<tr />').append(footerContent));

		thisTable.main.append(thisTable.footer);
	}

	// create data for offline sorting if not paging
	thisTable.rowData = new Array;
	$.each(thisTable.body.find('tr'), function(index) {
		var cells = $(this).find('td');
		var data = new Object;
		data.index = index;
		data.__$row = this;
		for ( var i = 0; i < thisTable.config.data_binds.length; i++) {
			var mappingConfig = thisTable.config.data_binds[i];

			var cellValue = $(cells[i + thisTable.header_index_offset]).text();

			if (mappingConfig.type == 'number') {
				cellValue = isNaN(cellValue) ? cellValue : parseInt(cellValue, 10);
			}

			data[mappingConfig.name] = cellValue;
		}
		thisTable.rowData.push(data);
	});

	if (thisTable.config.sortable) {
		// apply sort display
		for ( var i = 0; i < this.sortControls.length; i++) {
			if (this.sortControls[i].mapping == this.sort) {
				this.sortControls[i].direction = this.sort_direction;
				this.sortControls[i].applySort();
			} else {
				this.sortControls[i].removeSort();
			}
		}
	}

	if (isFunction(this.config.post_load)) {
		this.config.post_load(this);
	}
};
me.dapps.table.prototype.createPagingButton = function(current, total, footer) {
	var thisTable = this;

	if (current > 0) {
		// add previous, first
		var first = $('<div />').addClass('dapps-table-first-page').text('<<');

		first.click(function() {
			thisTable.paging(0);
		});

		var previous = $('<div />').addClass('dapps-table-previous-page').text('<');

		previous.click(function() {
			thisTable.paging(current - 1);
		});

		footer.append(first).append(previous);
	}

	if (current < total - 1) {
		// add next, last
		var last = $('<div />').addClass('dapps-table-last-page').text('>>');

		last.click(function() {
			thisTable.paging(total - 1);
		});

		var next = $('<div />').addClass('dapps-table-next-page').text('>');

		next.click(function() {
			thisTable.paging(current + 1);
		});

		footer.append(last).append(next);
	}
};
me.dapps.table.prototype.buildUrl = function(requestUrl, params) {
	var queryString = params.join('&');
	var appendCharacter = requestUrl.indexOf('?') > -1 ? '&' : '?';
	return requestUrl + appendCharacter + 'rand=' + new Date().getTime() + '&' + queryString;
};
me.dapps.table.prototype.paging = function(page) {
	this.page = page;
	this.loadData();
};
me.dapps.table.prototype.search = function(queries) {
	if (isArray(queries)) {
		this.queries = queries;

		if (this.config.paging || (!this.config.paging && !this.is_loaded)) {
			this.page = 0;
			this.loadData();
		} else {
			this.localSearch();
		}
	}
};
me.dapps.table.prototype.localSearch = function() {
	var hasResult = false;

	for ( var rowIndex = 0; rowIndex < this.rowData.length; rowIndex++) {
		// get rowdata
		var rowData = this.rowData[rowIndex];
		var rowCompareResult = true;
		for ( var queryIndex = 0; queryIndex < this.queries.length && rowCompareResult; queryIndex++) {
			var query = this.queries[queryIndex];

			var queryCompareResult = false;

			if (isSet(query.query_type) || query.query_type == 'all') {
				queryCompareResult = false;
				for (column in this.config.data_binds) {
					if (!queryCompareResult) {
						var compareValue = query.query_value.replace(/－/g,'ー');
						var baseValue = rowData[this.config.data_binds[column].name].replace(/－/g,'ー');
						var comparison = this.config.data_binds[column].comparison;

						if (isUnset(compareValue) || compareValue.length == 0) {
							queryCompareResult = true;
						} else {
							queryCompareResult = this.compare(compareValue, baseValue, comparison);
						}
					} else {
						break;
					}
				}
			} else {
				var comparison = null;
				for (column in this.config.data_binds) {
					if (this.config.data_binds[column].name == query.query_name) {
						comparison = this.config.data_binds[column].comparison;
						break;
					}
				}

				// get exactly value by query_name
				var value = rowData[query.query_name].replace(/－/g,'ー');
				var compareValue = query.query_value.replace(/－/g,'ー');

				if (isUnset(compareValue) || compareValue.length == 0) {
					queryCompareResult = true;
				} else {
					queryCompareResult = this.compare(compareValue, value, comparison);
				}
			}

			rowCompareResult &= queryCompareResult;
		}

		if (rowCompareResult) {
			$(rowData.__$row).show();
			hasResult = true;
		} else {
			$(rowData.__$row).hide();
		}
	}

	this.body.find('.dapps-table-no-data-row').remove();

	if (!hasResult) {
		this.displayNoData();
	}
};
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
me.dapps.table.prototype.clearParameterFromUrl = function() {
	var urls = (window.location + '').split('#');
	window.location.href = urls[0] + '#';
};
me.dapps.table.prototype.appendParametersToUrl = function(hashes) {
	if (this.config.use_hashes) {
		var urls = (window.location + '').split('#');
		var hashArr = new Array;

		for (param in hashes) {
			if (isArray(hashes[param])) {

			}
			var value = isArray(hashes[param]) ? JSON.stringify(hashes[param]) : hashes[param];
			hashArr.push(param + '=' + value);
		}

		window.location.href = urls[0] + '#' + hashArr.join('&');

		// store hashes
		this.hashes = window.location.href;
	}
};