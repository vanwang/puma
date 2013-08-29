(function(){
	UserManagementModule = {
			contenturl : "usermanagement.html",
			closable:true,
			init:init,
			onclose:onClose
	};
	
	function onClose(){
		console.log("close user-management-module");
	}
	
	function init(){
		bindToolbarAction();
		$('#user-grid').datagrid({  
	    	fit:true,
	    	idField:'id',
	    	fitColumns:true,
	    	pagination:true,
	    	pageSize:20,
	    	singleSelect:true,
	    	selectOnCheck:false,
	    	checkOnSelect:false,
	        url:'member/membertable.json',  
	        //toolbar:toolbar,
	        columns:[[  
	            {field:'ck',checkbox:true},
	            {field:'name',title:'姓名',sortable:true,width:120},  
	            {field:'email',title:'邮箱',sortable:true,width:120},  
	            {field:'lastConnect',title:'上次登录时间',sortable:true,width:120,align:'right'},
	            {field:'sys',title:'是否管理员',sortable:true,width:80,align:'right',formatter:formatSys},
	            {field:'active',title:'是否激活',sortable:true,width:60,formatter:formatActive},
	            {field:'createDate',title:'创建时间',sortable:true,width:120,align:'right'}
	        ]],
	        onDblClickRow:editMemberHandler
	    }); 
	}
	
	function formatSys(val,row){
		if(val){
			return "<span style='color:red;'>是</span>";
		}else{
			return "<span>否</span>";
		}
	}
	function formatActive(val,row){
		if(val){
			return "<span>已激活</span>";
		}else{
			return "<span style='color:red;'>未激活</span>";
		}
	}

	function bindToolbarAction(){
		$("#user-management-toolbar-add-new-id").bind("click",function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				addMemberHandler();
			}
		});
		$("#user-management-toolbar-edit-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				editMemberHandler();
			}
		});
		$("#user-management-toolbar-search-id").click(function(){
			alert('查询功能还未想好以什么样的形式来展示，如果您有好的建议，欢迎提供！我们会很快实现该功能！');
		});
		$("#user-management-toolbar-delete-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				deleteButtonHander();
			}
		});
		$("#user-management-toolbar-reset-password-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				resetPasswordButtonHander();
			}
		});
		$("#user-management-toolbar-reset-refresh-id").click(function(){
			$('#user-grid').datagrid("reload");
		});
	}
	
    /*var toolbar = [{  
        text:'新增',  
        iconCls:'icon-suppliers',  
        handler:addMemberHandler    
    },{  
        text:'编辑',  
        iconCls:'icon-edit',  
        handler:editMemberHandler
    },{  
        text:'查询',  
        iconCls:'icon-search',  
        handler:function(){alert('查询功能还未想好以什么样的形式来展示，如果您有好的建议，欢迎提供！我们会很快实现该功能！')}  
    },{  
        text:'删除',  
        iconCls:'icon-remove',  
        handler:deleteButtonHander
    },{  
        text:'重设密码',  
        iconCls:'icon-resetpass',  
        handler:resetPasswordButtonHander
    },'-',{  
        text:'刷新',  
        iconCls:'icon-refresh',  
        handler:function(){$('#user-grid').datagrid("reload");}  
    }];  */
    
    function getMemberFormContent(formId){
    	var $content = $("<form>").addClass("puma-form").attr("id",formId).attr("method","POST");
		
		$content.append($.getFormItem("用户名",$("<input type='text' name='name' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入用户名'")));
		$content.append($.getFormItem("用户邮箱",$("<input type='text' name='email' class='easyui-validatebox'>").attr("data-options","required:true,validType:'email',missingMessage:'请输入用户邮箱'")));
		$content.append($.getFormItem("密码",$("<input type='text' name='password' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入密码'")));
		$content.append($.getFormItem("重复密码",$("<input type='text' name='repassword' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请再次输入密码'")));
		$content.append($.getFormItem("是否启用",$("<input type='checkbox' name='active' checked='true'>")));
		$content.append($.getFormItem("是否管理员",$("<input id='is-admin-checkbox-id' type='checkbox' name='sys'>")));
		$content.append($.getFormItem("用户角色",$("<input id='role-combotree-id' name='roleIds[]' style='width:300px;'>"),"role-combotree-container-id"));
		$content.append($("<input name='id' type='hidden'>"));
		
		return $content;
    }
    
    function getMemberViewFormContent(formId){
    	var $content = $("<form>").addClass("puma-form").attr("id",formId).attr("method","POST");
		
		$content.append($.getFormItem("用户名",$("<input type='text' name='name' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入用户名'")));
		$content.append($.getFormItem("用户邮箱",$("<input type='text' name='email' class='easyui-validatebox'>").attr("data-options","required:true,validType:'email',missingMessage:'请输入用户邮箱'")));
		$content.append($.getFormItem("是否启用",$("<input type='checkbox' name='active' checked='true'>")));
		$content.append($.getFormItem("是否管理员",$("<input id='is-admin-checkbox-id' type='checkbox' name='sys'>")));
		$content.append($.getFormItem("用户角色",$("<input id='role-combotree-id' name='roleIds[]' style='width:300px;'>"),"role-combotree-container-id"));
		$content.append($("<input name='id' type='hidden'>"));
		
		return $content;
    }
    
    function editMemberHandler(){
    	//var node = $('#user-grid').datagrid("getSelected");
    	var	node = $('#user-grid').datagrid("getChecked");
		if(node.length == 0){
			node = $('#user-grid').datagrid("getSelected");
			if(node == null){
    			alert("请选择用户!");
        		return;
			}
		}else{
			node = node[0];
		}
    	
    	viewMemberHandler(node);
    }
    
    function viewMemberHandler(row){
    	var windowId = "view-member-window-id";
    	var formId = "view-member-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getMemberViewFormContent(formId);
    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
    			title:"编辑用户",
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
						
						$.loading("正在保存用户信息...");
    					
    					var url = "member/update.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
									$.messaging("编辑用户成功!",true);
									$('#user-grid').datagrid('reload');
									$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','编辑用户发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','编辑用户发生错误，错误原因:<br>'+data.statusText,'error');  
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
    				var $container = $("#role-combotree-container-id");
    			   $('#is-admin-checkbox-id').bind('click', function(){  
    				   if ($(this).is(":checked")) {
    					   $container.hide();
                       }else{
                    	   $container.show();
                       }
    			    });
    			   
    			   $("#role-combotree-id").combotree({  
    				    url: 'role/memberroletree.json?id='+row.id,  
    				    required: false  ,
    				    multiple:true,
    				    editable:false
    				});  
    			}
        	});  
    	}else{
    		$("#"+windowId).window("center");
    	}
    	//加载所选择的数据
    	$form = $("#"+formId);
    	$('input[name="id"]', $form).val(row.id);
    	$('input[name="name"]', $form).val(row.name);
    	$('input[name="email"]', $form).val(row.email);
    	$('input[name="sys"]', $form).attr("checked",row.sys);
    	$('input[name="active"]', $form).attr("checked",row.active);
    	
       var $container = $("#role-combotree-container-id");
	   if(row.sys){
		   $container.hide();
	   }else{
		   $container.show();
	   }
    };
    
    function addMemberHandler(){
    	var windowId = "add-new-member-window-id";
    	var formId = "add-new-member-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getMemberFormContent(formId);
    		
    		$("<div>").attr("id",windowId).dialog({ 
    			title:"新增用户",
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
    					//if you do not check the checkbox, undefined value will be returned
    					//TODO: will comment this in product edition
    					/*if($('#is-admin-checkbox-id').attr("checked")){
    						alert("体验版不允许添加系统管理员!");
    						return false;
    					}*/
    					var isValid = $("#"+formId).form('validate');
						if (!isValid){
							$.unloading();
							return false;
						}
						
    					$.loading("正在添加用户...");
    					
    					var url = "member/createnew.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
									$.messaging("新增用户成功!",true);
									$('#user-grid').datagrid('reload');
									$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','新增用户发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','新增用户发生错误，错误原因:<br>'+data.statusText,'error');  
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
    				   var $container = $("#role-combotree-container-id");
	     			   $('#is-admin-checkbox-id').bind('click', function(){  
	     				   if ($(this).is(":checked")) {
	     					   	$container.hide();
	                        }else{
	                        	$container.show();
	                        }
	     			    });
	     			   
	     			   $("#role-combotree-id").combotree({  
	     				    url: 'role/roletree.json',  
	     				    required: false,
	     				    multiple:true,
	     				   editable:false
	     				});  
    			}
        	});  
    	}else{
    		$("#"+windowId).window("center");
    	}
    };
    
    function deleteButtonHander(){
    	var node = $('#user-grid').datagrid("getChecked");
    	if(node.length == 0){
    		alert("请选择用户!");
    		return;
    	}
    	var idArr = new Array();
		for(var i = 0; i < node.length; i++){
			if(node[i].id === "00000000000000000000000000000000"){
				alert("不允许删除系统默认管理员!");
				return false;
			}
			idArr.push(node[i].id);
		}
		var dataObj = new Object();
    	dataObj.ids = idArr;
    	var url = "member/batchdel.do";
    	var msg = "确认要删除吗?";
    	
    	$.messager.confirm('删除', msg, function(r){  
            if (r){  
            	$.loading("正在删除用户...");
            	$.ajax( {
                    "dataType": 'JSON',
                    "type": "POST",
                    "url": url,
                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                    "data": dataObj,
                    "success": function(data){
                    	$.unloading();
                    	 if(data.status == "success"){
                    		 $('#user-grid').datagrid("reload");
                    		 $('#user-grid').datagrid("clearSelections");
        	          	  }else{
        	          	  }
                    	 $.messaging("删除用户成功!",true);
                    },
                    "error":function(data){
                    	$.unloading();
                    	$.messager.alert('错误','删除用户失败,请重试，错误原因:<br>'+data.statusText,'error');  
        			}
                  } );
            }  
        });  
    	
    }
    
    function resetPasswordButtonHander(){
    	var node = $('#user-grid').datagrid("getChecked");
    	if(node.length == 0){
    		alert("请选择用户!");
    		return;
    	}
    	var idArr = new Array();
		for(var i = 0; i < node.length; i++){
			idArr.push(node[i].id);
		}
		var dataObj = new Object();
    	dataObj.ids = idArr;
    	var url = "member/batchresetpass.do";
    	var msg = "确认要重设密码吗?";
    	
    	$.messager.confirm('重设密码', msg, function(r){  
            if (r){  
            	$.loading("正在重设密码...");
            	$.ajax( {
                    "dataType": 'JSON',
                    "type": "POST",
                    "url": url,
                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                    "data": dataObj,
                    "success": function(data){
                    	$.unloading();
                    	 if(data.status == "success"){
                    		 //$('#user-grid').datagrid("reload");
        	          	  }else{
        	          	  }
                    	 $.messaging("重设密码成功!",true);
                    },
                    "error":function(data){
                    	$.unloading();
                    	$.messager.alert('错误','重设密码失败,请重试，错误原因:<br>'+data.statusText,'error');  
        			}
                  } );
            }  
        });  
    	
    }
})();
