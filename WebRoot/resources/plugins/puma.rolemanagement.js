(function(){
	var loadedRole = null;
	RoleManagementModule = {
			contenturl : "rolemanagement.html",
			init:init,
			onclose:onClose
	};
	
	function init(){
		initButtons();
		
		//初始化角色树
		$("#role-tree-id").tree({  
		    url: 'role/roletree.json',
		    onSelect:onSelectRoleTree,
		    onDblClick:onDblClickRoleTree,
		    onDragOver:function(target, source, point){
		    	//console.log(target);
		    	if($(target).hasClass("tree-node-append")){
		    		$("span.tree-dnd-icon").removeClass("tree-dnd-yes").addClass("tree-dnd-no");
		    	}
		    },
		    onLoadSuccess:function(node, data){
		    	$("#role-tree-id").sortable({ cursor: "move", axis: "y", start: function(event, ui){
		    		$(ui.item).children().css("cursor","move")
		    		//console.log($(ui.item).children().css("cursor","move"));
		    	},stop:function(event, ui){
		    		$(ui.item).children().css("cursor","pointer")
		    		//console.log($(ui.item).children().css("cursor","pointer"));
		    	} });
		    	
				if(loadedRole != null){
					 var node = $("#role-tree-id").tree('find', loadedRole);
					 loadedRole = null;
					 if(node != null){
						 $("#role-tree-id").tree('select', node.target);
					 }
				}
		    }
		}); 
		//初始化资源数
		$("#resource-tree-id").tree({  
		    url: 'resource/resourcetree.json',
		    checkbox:true,
		    onLoadSuccess:function(){
				$("#resource-tree-id > li > ul > li").addClass("haha");
				//$("#resource-tree-id > li > ul > li:last-child").addClass("haha2");
				$("#resource-tree-id > li > div").css("clear","left");
		    }
		}); 
	}
	function onClose(){
		console.log("close rolemanagement page");
	}
	
	function onDblClickRoleTree(node){
		viewRoleHandler(node);
	}
	
	function onSelectRoleTree(node){
		var roleId = node.id;
		selectRoleTree(roleId);
	}
	
	function selectRoleTree(roleId){
		if(loadedRole == roleId){
			return;
		}
		
		$.loading("正在加载资源...");
		loadedRole = roleId;
		var dataObj = new Object();
    	dataObj.roleId = roleId;
    	
		$.ajax( {
            "dataType": 'JSON',
            "type": "POST",
            "url": "role/selectedresource.do",
            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
            "data": dataObj,
            "success": function(data){
            	$.unloading();
            	 if(data.status == "success"){
            		 var ids = data.message;
            		 //clear all
            		 var arr = $("#resource-tree-id").tree("getRoots");
    				 for(var i = 0; i < arr.length; i++){
    					$("#resource-tree-id").tree("uncheck",arr[i].target);
    				 }
    				 /*
            		 if(ids.length == 0){
            			 var arr = $("#resource-tree-id").tree("getRoots");
        				 for(var i = 0; i < arr.length; i++){
        					$("#resource-tree-id").tree("uncheck",arr[i].target);
        				 }
            			 return;
            		 }*/
            		 for(var i = 0; i < ids.length; i ++){
            			 var node = $("#resource-tree-id").tree('find', ids[i]);
            			 if(node != null){
            				 $("#resource-tree-id").tree('check', node.target);
            			 }
            		 }
	          	  }else{
	          	  }
            },
            "error":function(data){
            	$.unloading();
            	$.messager.alert('错误','加载资源失败,请重试，错误原因:<br>'+data.statusText,'error');  
			}
          } );
	
	}
	
	function getRoleFormContent(formId){
    	var $content = $("<form>").addClass("puma-form").attr("id",formId).attr("method","POST");
		
		$content.append($.getFormItem("角色名称",$("<input type='text' name='name' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入角色名称!'")));
		//$content.append($.getFormItem("是否启用",$("<input type='checkbox' name='active' checked='true'>")));
		//$content.append($.getFormItem("是否后台权限",$("<input type='checkbox' name='sys' checked='true'>")));
		$content.append($.getFormItem("描述",$("<textarea name='description' style='width:90%;'>")));
		$content.append($("<input name='id' type='hidden'>"));
		
		return $content;
    }
	
	function viewRoleHandler(row){
    	var windowId = "view-role-window-id";
    	var formId = "view-role-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getRoleFormContent(formId);
    		
    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
    			title:"编辑角色",
        	    width:500,  
        	    height:320,  
        	    modal:false,
        	    collapsible:false,
        	    minimizable:false,
        	    maximizable:false,
        	    resizable:true,
        	    content:$content,
        	    buttons:[{
    				text:'保存',
    				handler:function(){


    					var isValid = $("#"+formId).form('validate');
						if (!isValid){
							$.unloading();
							return false;
						}
						
						$.loading("正在保存角色...");
    					
    					var url = "role/update.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
    	                    		$.messaging("编辑角色成功!",true);
    								loadedRole = data.message;
    								$("#role-tree-id").tree("reload");
    	                   			$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','编辑角色发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','编辑角色发生错误，错误原因:<br>'+data.statusText,'error');  
    	        			}
    	                  } );
    				}
    			},{
    				text:'取消',
    				handler:function(){$("#"+windowId).window("destroy");}
    			}],
    			onClose:function(){
    				$(this).window("destroy");
    			},
    			onOpen:function(){}
        	});  
    	}else{
    		$("#"+windowId).window("center");
    	}
    	//console.log(row);
    	//加载所选择的数据
    	$form = $("#"+formId);
    	$('input[name="id"]', $form).val(row.id);
    	$('input[name="name"]', $form).val(row.text);
    	$('textarea[name="description"]', $form).val(row.attributes.description);
    	//$('input[name="sys"]', $form).attr("checked",row.attributes.sys);
    	//$('input[name="active"]', $form).attr("checked",row.attributes.active);
    };
    
    function addRoleHandler(){
    	var windowId = "add-new-role-window-id";
    	var formId = "add-new-role-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getRoleFormContent(formId);
    		
    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
    			title:"新增角色",
        	    width:500,  
        	    height:320,  
        	    modal:true,
        	    collapsible:false,
        	    minimizable:false,
        	    maximizable:false,
        	    resizable:true,
        	    content:$content,
        	    buttons:[{
    				text:'保存',
    				handler:function(){

    					var isValid = $("#"+formId).form('validate');
						if (!isValid){
							$.unloading();
							return false;
						}
						
						$.loading("正在添加角色...");
    					
    					var url = "role/createnew.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
    	                    		$.messaging("添加角色成功!",true);
    								loadedRole = data.message;
    								$("#role-tree-id").tree("reload");
    	                   			$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','添加角色发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','添加角色发生错误，错误原因:<br>'+data.message,'error');  
    	        			}
    	                  } );
    				}
    			},{
    				text:'取消',
    				handler:function(){$("#"+windowId).window("destroy");}
    			}],
    			onClose:function(){
    				$(this).window("destroy");
    			},
    			onOpen:function(){
    			}
        	});  
    	}else{
    		$("#"+windowId).window("center");
    	}
    };
    
	function initButtons(){
		$("#add-role-btn-id").bind('click', function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				addRoleHandler();
			}
		}); 
		
		$("#delete-role-btn-id").bind('click', function(){  
			var opt = $(this).linkbutton('options');
			if(opt.disabled){
				return false;
			}
			
			var role = $("#role-tree-id").tree("getSelected");
			if(role == null){
				alert("请选择相要删除的角色!");
				return;
			}
			$.messager.confirm('删除角色', '确认删除角色【'+role.text+'】吗?', function(r){  
                if (r){  
                	$.loading("正在删除角色...");
                	
                	var idArr = new Array();
        			idArr.push(role.id);
        			
                	var dataObj = new Object();
                	dataObj.ids = idArr;
                	$.ajax( {
        	            "dataType": 'JSON',
        	            "type": "POST",
        	            "url": "role/batchdel.do",
        	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
        	            "data": dataObj,
        	            "success": function(data){
        	            	$.unloading();
        	            	 if(data.status == "success"){
        		          	  }else{
        		          	  }
        	            	 
        	            	 loadedRole = null;
        	            	 $("#role-tree-id").tree("reload");
        	            	 
        	            	 $.messaging("删除角色成功!",true);
        	            	 
        	            },
        	            "error":function(data){
        	            	$.unloading();
	                    	$.messager.alert('错误','删除角色失败,请重试，错误原因:<br>'+data.statusText,'error');  
        				}
        	          } );
                }  
            });  
	    }); 
		
		$("#edit-role-btn-id").bind('click', function(){  
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				var role = $("#role-tree-id").tree("getSelected");
				viewRoleHandler(role);
			}
	    });
		
		//选择所有
		$("#select-all-resrouces-btn-id").bind('click', function(){  
			var arr = $("#resource-tree-id").tree("getRoots");
			for(var i = 0; i < arr.length; i++){
				$("#resource-tree-id").tree("check",arr[i].target);
			}
	    });
		//全不选
		$("#unselect-all-resrouces-btn-id").bind('click', function(){  
			var arr = $("#resource-tree-id").tree("getRoots");
			for(var i = 0; i < arr.length; i++){
				$("#resource-tree-id").tree("uncheck",arr[i].target);
			}
	    });
		//全部展开
		$("#expand-all-resrouces-btn-id").bind('click', function(){  
			$("#resource-tree-id").tree("expandAll");
	    });
		//全部收起
		$("#collapse-all-resrouces-btn-id").bind('click', function(){  
			$("#resource-tree-id").tree("collapseAll");
	    });
		//刷新
		$("#refresh-resrouces-btn-id").bind('click', function(){  
	    	$("#resource-tree-id").tree("reload");
	    	if(loadedRole != null){
	    		var role = $("#role-tree-id").tree("getSelected");
	    		loadedRole = null;
	    		setTimeout(function(){
	    			selectRoleTree(role.id);
	    		},1000);
	    	}
	    }); 
		//保存设置
		$("#save-role-resrouces-btn-id").bind('click', function(){  
			var opt = $(this).linkbutton('options');
			if(opt.disabled){
				return false;
			}
			
			$.loading("正在保存资源设置...");
			
			var role = $("#role-tree-id").tree("getSelected");
			var roleId = null;
			if(role == null){
				alert("请选择相对应的角色!");
				return;
			}else{
				roleId = role.id;
			}
			var arr = $("#resource-tree-id").tree("getChecked");
			var idArr = new Array();
			for(var i = 0; i < arr.length; i++){
				if(arr[i].attributes != undefined){
					if(arr[i].attributes.type == "resource"){
						idArr.push(arr[i].id);
					}
				}
			}
			var dataObj = new Object();
        	dataObj.ids = idArr;
        	dataObj.roleId = roleId;
        	$.ajax( {
	            "dataType": 'JSON',
	            "type": "POST",
	            "url": "role/combineresource.do",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": dataObj,
	            "success": function(data){
	            	$.unloading();
	            	 if(data.status == "success"){
		          	  }else{
		          	  }
	            	 $.messaging("保存资源设置成功!",true);
	            },
	            "error":function(data){
	            	$.unloading();
                	$.messager.alert('错误','保存资源设置失败,请重试，错误原因:<br>'+data.statusText,'error');  
				}
	          } );
	    });
	}
	
})();
