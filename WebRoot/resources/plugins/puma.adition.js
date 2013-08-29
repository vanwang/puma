(function(){
	AditionModule = {
			tabid : "unique",
			moduleid : "system-adition-module-id",
			title : "个人设置",
			leftmenu : "member-config-menu",
			linkselectorid : "link-AditionModule",
			contenturl : "welcome.html",
			closable:true,
			init:init,
			onclose:onClose
	};
	
	function init(){
		console.log("init adition home page");
	}
	
	function onClose(){
		console.log("init adition home page");
	}
})();
