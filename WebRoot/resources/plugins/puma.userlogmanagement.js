(function(){
	UserLogManagementModule = {
			contenturl : "userlog.html",
			closable:true,
			init:init,
			onclose:onClose
	};
	
	function onClose(){
		console.log("close userlog-management-module");
	}
	
	function init(){
		bindToolbarAction();
		$('#user-log-grid').datagrid({  
	    	fit:true,
	    	idField:'id',
	    	fitColumns:true,
	    	pagination:true,
	    	pageSize:20,
	    	singleSelect:true,
	    	selectOnCheck:false,
	    	checkOnSelect:false,
	        url:'log/logtable.json',  
	        //toolbar:toolbar,
	        columns:[[  
	            {field:'ck',checkbox:true},
	            {field:'mname',title:'操作人',sortable:true,width:120},  
	            {field:'type',title:'操作类型',sortable:true,width:120},
	            {field:'level',title:'日志级别',sortable:true,width:120},  
	            {field:'message',title:'日志信息',sortable:true,width:120},  
	            {field:'createDate',title:'操作时间',sortable:true,width:120,align:'right'},
	        ]]
	    }); 
	}
	

	function bindToolbarAction(){
		$("#user-log-management-toolbar-search-id").click(function(){
			alert('查询功能还未想好以什么样的形式来展示，如果您有好的建议，欢迎提供！我们会很快实现该功能！');
		});
		$("#user-log-management-toolbar-delete-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				deleteButtonHander();
			}
		});
		$("#user-log-management-toolbar-reset-refresh-id").click(function(){
			$('#user-log-grid').datagrid("reload");
		});
	}
	
    function deleteButtonHander(){
    	var node = $('#user-log-grid').datagrid("getChecked");
    	if(node.length == 0){
    		alert("请选择要删除的日志!");
    		return;
    	}
    	var idArr = new Array();
		for(var i = 0; i < node.length; i++){
			idArr.push(node[i].id);
		}
		var dataObj = new Object();
    	dataObj.ids = idArr;
    	var url = "log/batchdel.do";
    	var msg = "确认要删除吗?";
    	
    	$.messager.confirm('删除日志', msg, function(r){  
            if (r){  
            	$.loading("正在删除用户日志...");
            	$.ajax( {
                    "dataType": 'JSON',
                    "type": "POST",
                    "url": url,
                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                    "data": dataObj,
                    "success": function(data){
                    	$.unloading();
                    	 if(data.status == "success"){
                    		 $('#user-log-grid').datagrid("reload");
                    		 $('#user-log-grid').datagrid("clearSelections");
        	          	  }else{
        	          	  }
                    	 $.messaging("删除用户日志成功!",true);
                    },
                    "error":function(data){
                    	$.unloading();
                    	$.messager.alert('错误','删除用户日志失败,请重试，错误原因:<br>'+data.statusText,'error');  
        			}
                  } );
            }  
        });  
    	
    }
    
})();
