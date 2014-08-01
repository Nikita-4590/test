if (isSet($)) {
	// inject to jQuery
	$.fn.extend({
		box : function(config) {
			var instance = new me.dapps.box(config);

			instance.showFromHtml(this);

			return instance;
		}
	});
}

me.dapps.box = function(config) {
	this.config = config == null ? new Object : config;

	this.default_config = {
		title : undefined,
		width : 400,
		height : 300,
		draggable : true,
		background : '#000',
		background_opacity : 0.5,
		z_index : 1000,
		auto_hide : false,
		close_button : true,
		loading_text : 'Loading...',
		pre_close : undefined,
		pre_open : undefined,
		post_close : undefined,
		post_open : undefined,
		button : undefined,
		type: undefined
	};

	for (key in this.default_config) {

		if (isUnset(this.config[key])) {
			this.config[key] = this.default_config[key];
		}
	}

	this.id = new Date().getTime();
};
me.dapps.box.prototype.show = function(content) {
	// create a reference for this instance
	var thisBox = this;
	
	var classIcons = {
		'WARNING' : 'message-icon-wrn',
		'ERROR'  : 'message-icon-err'
	};
	
	if (isSet(classIcons[this.config.type])) {
		  ICONS = $('<div />').attr('class', classIcons[this.config.type]);
	}
	
	if (isSet(this.config.type)) {
		// create content have image
		this.content = $('<div />').attr('class', 'dapps-box-content').append(ICONS).append(content);
	} else {
		// create content don't have image
		this.content = $('<div />').attr('class', 'dapps-box-content').append(content);
	}
	
	var classMains = {
		'WARNING' : 'dapps-box-main-wrn',
		'ERROR'  : 'dapps-box-main-err'
	};
			
	if (isSet(classMains[this.config.type])) {
		MAINS = $('<div />').attr('id', this.id).attr('class', classMains[this.config.type]).attr('tabindex', -1);
	}
	
	if (isSet(this.config.type)) {
		this.main = MAINS;
	} else {
		this.main = $('<div />').attr('id', this.id).attr('class', 'dapps-box-main').attr('tabindex', -1);
	}

	this.main.css({
		'z-index' : this.config.z_index
	});
	
	var classHeaders = {
		'WARNING' : 'dapps-box-header-wrn',
		'ERROR'  : 'dapps-box-header-err'
	};
		
	if (isSet(classHeaders[this.config.type])) {
		HEADERS = $('<div />').attr('class', classHeaders[this.config.type]);
	}
	
	if (isSet(this.config.type)) {
		this.header = HEADERS;
	} else {
		this.header = $('<div />').attr('class', 'dapps-box-header');
	}

	if (this.config.close_button) {
		// inject close button
		var closeButton = $('<div />');
		closeButton.attr('class', 'dapps-box-close-button').click(function() {
			thisBox.close();
		});

		this.header.append(closeButton);
	}
	
	var classTitles = {
		'WARNING' : 'dapps-box-title-wrn',
		'ERROR'  : 'dapps-box-title-err'
	};
			
	if (isSet(classTitles[this.config.type])) {
		TITLES = $('<div />').text(this.config.title).addClass(classTitles[this.config.type]);
	}

	if (isSet(this.config.title)) {
		var title = null;
		if (isSet(this.config.type)) {
			title = TITLES;
		} else {
			title = $('<div />').text(this.config.title).addClass('dapps-box-title');
		}
		
		this.header.append(title);
	}

	this.main.append(this.header);
	this.main.append(this.content);

	// create overlay
	this.overlay = $('<div />').attr('id', 'overlay-' + this.id);
	this.overlay.attr('class', 'dapps-box-overlay').css({
		'background' : this.config.background,
		'opacity' : this.config.background_opacity,
		'z-index' : this.config.z_index
	});

	if (this.config.auto_hide) {

		this.overlay.click(function() {
			thisBox.close();
		});
	}

	// change all overlay to transparent
	$.each($('.dapps-box-overlay'), function() {
		// store old background
		var opacity = $(this).css('opacity');
		$(this).data('opacity', opacity).css('opacity', 0);
	});

	// add buttons
	if (isSet(this.config.button)) {
		if (isArray(this.config.button.list)) {
			var align = this.config.button.align;

			this.buttons = new Array;

			for ( var i = 0; i < this.config.button.list.length; i++) {
				var config = this.config.button.list[i];

				if (isSet(config.text)) {
					var button = $('<button />').attr('type', 'button').addClass('dapps-box-button').text(config.text);
					
					if (isSet(config.loading) && config.loading) {
						button.addClass('dapps-box-button-loading');
					}

					if (isFunction(config.action)) {
						button.data('index', i);
						button.bind('click', function() {
							var index = parseInt($(this).data('index'), 10);
							thisBox.config.button.list[index].action(thisBox);
						});
					}

					this.buttons.push(button);
				}
			}

			if (this.buttons.length > 0) {
				// append buttons
				this.footer = $('<div />').css('text-align', isSet(align) ? align : 'left').addClass('dapps-box-footer');
				for ( var i = 0; i < this.buttons.length; i++) {
					this.footer.append(this.buttons[i]);
				}
				this.main.append(this.footer);
			}
		}
	}

	if (isFunction(this.config.pre_open)) {
		if (this.config.pre_open(this)) {
			$('body').append(this.overlay);
			$('body').append(this.main);

			this.adjustPosition(false);
		} else {
			return null;
		}
	} else {

		$('body').append(this.overlay);
		$('body').append(this.main);

		this.adjustPosition(false);
	}

	this.main.focus();

	if (isFunction(this.config.post_open)) {
		this.config.post_open(this);
	}
};
me.dapps.box.prototype.children = function(selector) {
	return this.main.find(selector);
};
me.dapps.box.prototype.beginLoad = function() {
	this.show(this.config.loading_text);
};
me.dapps.box.prototype.endLoad = function(content, callback) {
	this.content.empty().append(content);
	this.adjustPosition(false);

	if (isFunction(callback)) {
		callback(this);
	}
};
me.dapps.box.prototype.showFromHtml = function(content) {
	content = $(content).clone().removeAttr('id').show();

	this.show(content);
};
me.dapps.box.prototype.showFromUrl = function(params) {
	var thisBox = this;
	thisBox.beginLoad();
	// hide all button
	for (var i = 0; i < this.buttons.length; i++) {
		if (this.buttons[i].is('.dapps-box-button-loading')) {
			this.buttons[i].show();
		} else {
			this.buttons[i].hide();
		}
	}
	var appendCharacter = params.url.indexOf('?') > -1 ? '&' : '?';
	$.ajax({
		url : params.url + appendCharacter + 'rand=' + new Date().getTime(),
		data : params.data,
		type : params.method,
		success : function(response) {
			// var box = new me.dapps.box(params.config);
			// hide all button
			for (var i = 0; i < thisBox.buttons.length; i++) {
				if (thisBox.buttons[i].is('.dapps-box-button-loading')) {
					thisBox.buttons[i].hide();
				} else {
					thisBox.buttons[i].show();
				}
			}
			
			thisBox.endLoad(response, params.callback);
		},
		error : function(e) {
			if (isFunction(params.error)) {
				params.error(thisBox, e);
			}
		}
	});

};
me.dapps.box.prototype.hide = function() {
	this.main.remove();
	this.overlay.remove();

	// change last overlay to
	var overlay = $('.dapps-box-overlay:last');
	if (overlay.length > 0) {
		overlay.css('opacity', overlay.data('opacity'));
	}

	if (isFunction(this.config.post_close)) {
		this.config.post_close();
	}
};
me.dapps.box.prototype.close = function() {
	if (isFunction(this.config.pre_close)) {
		if (this.config.pre_close(this)) {
			this.hide();
		}
	} else {
		this.hide();
	}
};
me.dapps.box.prototype.adjustPosition = function(animate) {
	var boxW = this.main.outerWidth();
	var boxH = this.main.outerHeight();

	if (boxW < this.config.width) {
		this.main.outerWidth(this.config.width);
		boxW = this.main.outerWidth();
	}

	// check boundary
	var css = {
		top : ($(window).height() - boxH) / 2 + $(window).scrollTop(),
		left : ($(window).width() - boxW) / 2 + $(window).scrollLeft()
	};
	
	if (css.top < 0) css.top = 0;
	if (css.left < 0) css.left = 0;

	if (animate) {
		this.main.animate(css, 'fast');
	} else {
		this.main.css(css);
	}

	// make draggable
	if (!this.main.is('.ui-draggable')) {
		this.main.draggable({
			containment : 'parent',
			cancel : '.dapps-box-header, .dapps-box-content, .dapps-box-footer',
			cursor : 'move'
		});
	}

	return;
};