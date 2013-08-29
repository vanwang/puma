(function(){
	ChangeSkinModule = {
			contenturl : "changeskin.html",
			closable:false,
			init:init
	};
	
	function init(){
		$("#change-skin-container-id .change-skin-btn-class").bind("click",function(){
			$.loading("正在应用皮肤");
			var theme = easyloader.theme;
			var newtheme = $(this).attr("data");
			$("body").addClass("noscroll");
			$("link[rel='stylesheet']").each(function(){
				var href = $(this).attr("href");
				var re = theme;
				var r = href.replace(re, newtheme);   
				$(this).attr("href",r);
			});
			easyloader.theme = newtheme;
			$.ajax( {
                "dataType": 'JSON',
                "type": "POST",
                "url": "member/changetheme.do",
                "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                "data": "theme="+newtheme,
                "success": function(data){
                	$.unloading();
                	 if(data.status == "success"){
    	          	  }else if(data.status == "warning"){
    	          		  alert("请先登录后再保存皮肤，当前设置未保存!");
    	          		  return;
    	          	  }
                	 $.messaging("皮肤设置成功!",true);
                },
                "error":function(data){
                	$.unloading();
                	$.messaging("皮肤设置失败!");
    			}
              } );
			$("body").removeClass("noscroll");
		});
	}
})();
