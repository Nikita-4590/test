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