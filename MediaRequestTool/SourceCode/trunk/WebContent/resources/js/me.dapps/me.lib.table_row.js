// CREATE OBJECT TR ELEMENT
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
