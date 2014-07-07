// CREATE OBJECT TD ELEMENT
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