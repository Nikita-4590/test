// CREATE OBJECT TH ELEMENT
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