;(function(){
	QrCodeModule = {
			contenturl : "qrcode.html",
			init:init,
			onclose:onClose,
			onresize:onResize
	};
	
	function init(param){
		$("#qrcode-module-generate-code-btn-id").bind("click",function(){
			var data = $("#qrcode_module_form").formSerialize();
			console.log(data);
			var url = "qrcode/generateqrimage.do?"+data;
			$("#qrcode-module-gen_url").attr("href",url).empty().html($("<img>").attr("src",url));
		});
		
	}
	function onClose(){
		console.log("close QrCodeModule page");
	}
	function onResize(){
	}
})();
