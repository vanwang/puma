;(function(){
	SystemHomeModule = {
			contenturl : "welcome.html",
			init:init,
			onclose:onClose,
			onresize:onResize
	};
	
	function init(param){
		if(param){
			if(param.data){
				$.updateMainTabTitle(param.data);
			}
		}
		console.log("init system home page");
	}
	function onClose(){
		console.log("close system home page");
	}
	function onResize(){
		$("#system-home-info-tab-id").tabs('resize');
	}
})();
