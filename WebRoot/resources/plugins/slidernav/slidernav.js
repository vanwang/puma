/*
 *  SliderNav - A Simple Content Slider with a Navigation Bar
 *  Copyright 2010 Monjurul Dolon, http://mdolon.com/
 *  Download by http://www.jb51.net
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://devgrow.com/slidernav
 */
$.fn.sliderNav = function(options) {
	var defaults = { items: ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"], debug: false, height: null, arrows: true};
	var opts = $.extend(defaults, options); 
	var o = $.meta ? $.extend({}, opts, $$.data()) : opts; 
	var slider = $(this); 
	$(slider).addClass('slider');
	$('.slider-content li:first', slider).addClass('selected');
	$(slider).append('<div class="slider-nav"><ul></ul></div>');
	
	for(var i in o.items) {
		if((typeof o.items[i]) != "function"){
			$('.slider-nav ul', slider).append("<li><a alt='#"+o.items[i]+"'>"+o.items[i]+"</a></li>");
		}
	}
	var height = $('.slider-nav', slider).height();
	
	if(o.height) {
		height = o.height;
	}
	
	$(window).on("resize",function(){
		hoverInResizer();
	});
	//$('.slider-content, .slider-nav', slider).css('height',height);
	
	var hoverInHandler = function(){
		$('.slide-up, .slide-down, .slider-nav', slider).show();
		$('.slider-content', slider).css('overflow',"auto");
		hoverInResizer();
	};
	
	var hoverOutHandler = function(){
		$('.slide-up, .slide-down, .slider-nav', slider).hide();
		hoverOutResizer();
	};
	
	var hoverInResizer = function(){
		$('.slider-content, .slider-nav', slider).css('height',$(slider).parent().height()-20);
		$(slider).css('height',$(slider).parent().height()-23);
		var liHeight = ($(slider).parent().height()-40-23)/$('.slider-nav ul li', slider).length;
		$('.slider-nav ul li', slider).css("height", Math.floor(liHeight));
	};
	
	var hoverOutResizer = function(){
		$('.slider-content', slider).css('overflow',"hidden").css('height',$(slider).parent().height());
	};
	
	setTimeout(function(){
		hoverOutHandler();
		$(slider).hover(function(){
			hoverInHandler();
		},function(){
			hoverOutHandler();
		});
		
		},1000);
	
	if(o.arrows){
		$('.slider-nav',slider).css('top','20px');
		$(slider).prepend('<div class="slide-up end"><span class="arrow up"></span></div>');
		$(slider).append('<div class="slide-down"><span class="arrow down"></span></div>');
		$('.slide-down',slider).click(function(){
			$('.slider-content',slider).animate({scrollTop : "+="+height+"px"}, 500);
		});
		$('.slide-up',slider).click(function(){
			$('.slider-content',slider).animate({scrollTop : "-="+height+"px"}, 500);
		});
	}
	if(o.debug){
		 $(slider).append('<div id="debug">Scroll Offset: <span>0</span></div>');
	}
	$('.slider-nav a', slider).click(function(event){
		var target = $(this).attr('alt');
		var cOffset = $('.slider-content', slider).offset().top;
		var tOffset = $('.slider-content '+target, slider).offset().top;
		var height = $('.slider-nav', slider).height(); 
		if(o.height) height = o.height;
		var pScroll = (tOffset - cOffset) - height/8;
		$('.slider-content li', slider).removeClass('selected');
		$(target).addClass('selected');
		$('.slider-content', slider).stop().animate({scrollTop: '+=' + pScroll + 'px'});
		if(o.debug){
			$('#debug span', slider).html(tOffset);
		}
	});
};