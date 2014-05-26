/*--- REGISTER UTILITIES FUNCTIONS TO JAVASCRIPT CORE ---*/
// isArray
window.isArray = function(testObject) {
	return Object.prototype.toString.call(testObject) === '[object Array]';
};
// isFunction
window.isFunction = function(testObject) {
	return typeof testObject === 'function';
};
// isSet
window.isSet = function(testObject) {
	return typeof testObject !== 'undefined' && testObject != null;
};
// isUnset
window.isUnset = function(testObject) {
	return typeof testObject === 'undefined' || testObject == null;
};
window.parseJSON = function (text) {
	try {
		var escapedText = text.replace(/'/g, "\"");
		return JSON.parse(escapedText);
	} catch (e) {
		return null;
	}
};
window.exists = function (name, scope) {
	var parent = scope || window;
	
	var frags = name.split('.');
	
	var isExisted = true;
	
	for (var i = 0; i < frags.length; i++) {
		if (isUnset(parent[frags[i]])) {
			isExisted = false;
			break;
		}
		parent = parent[frags[i]];
	}
	
	return isExisted;
};
/*--- INITIALIZE LIBRARY ---*/
// create namespace
if (typeof me == 'undefined') {
	var me = new Object;
}
if (typeof me.dapps == 'undefined') {
	me.dapps = new Object;
}

// create global variable container
if (typeof me.dapps.global == 'undefined') {
	me.dapps.global = new Object;
}