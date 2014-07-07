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
