(function(){
	var selectedId = null;
	ResourceManagementModule = {
			contenturl : "resourcemanagement.html",
			init:init,
			onclose:onClose
	};
	
	function onClose(){
		console.log("close resource-management-module");
	}
	
	function init(){
		bindToolbarAction();
		
		$('#resource-tree-grid').treegrid({  
	    	fit:true,
	    	fitColumns:true,
	    	pagination:false,
	    	rownumbers: false,  
	    	showFooter:true,
	    	showHeader:true,
	        url:'resource/resourcetreetable.json',  
	        idField:'id',  
		    treeField:'name',  
	        //toolbar:toolbar,
	        columns:[[  
	            {field:'name',title:'名称',sortable:false,width:100},  
	            {field:'url',title:'资源地址',sortable:false,width:200}
	            //{field:'active',title:'是否可用',sortable:false,width:90},  
	            //{field:'sys',title:'是否管理后台',sortable:false,width:80,align:'right'}
	        ]],
	        onDblClickRow:function(row){
	        	selectedId = row.id;
	        	if(row.type == "group"){
	        		editGroupname(row.name, row.id);
	        		return;
	        	}
	        	//if($("#view-resource-window-id").length == 0){
	        		viewResourceHandler(row);
	        	//}
	        },
	        onClickRow:function(row){
	        	selectedId = row.id;
	        	if(row.type == "group"){
	        		return;
	        	}
	        	if($("#view-resource-window-id").length > 0){
	        		viewResourceHandler(row);
	        	}
	        },
	        onLoadSuccess:function(){
	        	if(selectedId != null){
	        		$('#resource-tree-grid').treegrid("select",selectedId);
	        	}
	        	//TODO: Do not support dnd in Version 1.0
	        	//enableDnd($('#resource-tree-grid'));
	        }
	    }); 
	}
	
	function bindToolbarAction(){
		$("#resource-management-toolbar-add-new-id").bind("click",function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				addResourceHandler();
			}
		});
		$("#resource-management-toolbar-edit-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				editButtonHander();
			}
		});
		$("#resource-management-toolbar-delete-id").click(function(){
			var opt = $(this).linkbutton('options');
			if(!opt.disabled){
				deleteButtonHander();
			}
		});
		$("#resource-management-toolbar-expand-id").click(function(){
			$('#resource-tree-grid').treegrid("expandAll");
		});
		$("#resource-management-toolbar-collapse-id").click(function(){
			$('#resource-tree-grid').treegrid("collapseAll");
		});
		$("#resource-management-toolbar-refresh-id").click(function(){
			$('#resource-tree-grid').treegrid("reload");
		});
	}
	
    /*var toolbar = [{  
        text:'新增',  
        iconCls:'icon-add',  
        handler:addResourceHandler    
    },{  
        text:'编辑',  
        iconCls:'icon-edit',  
        handler:editButtonHander  
    },{  
        text:'删除',  
        iconCls:'icon-remove',  
        handler:deleteButtonHander  
    },'-',{  
        text:'全部展开',  
        iconCls:'icon-category',  
        handler:function(){$('#resource-tree-grid').treegrid("expandAll");}  
    },{  
        text:'全部收起',  
        iconCls:'icon-folder',  
        handler:function(){$('#resource-tree-grid').treegrid("collapseAll");}  
    },'-',{  
        text:'刷新',  
        iconCls:'icon-refresh',  
        handler:function(){$('#resource-tree-grid').treegrid("reload");}  
    }];  */
    
    function editButtonHander(){
    	var node = $('#resource-tree-grid').treegrid("getSelected");
    	if(node == null){
    		alert("请选择一个项目!");
    		return;
    	}
    	
    	if(node.type == "group"){
    		editGroupname(node.name, node.id);
    		return;
    	}
    	//if($("#view-resource-window-id").length == 0){
    		viewResourceHandler(node);
    	//}
    }
    
    function deleteButtonHander(){
    	var node = $('#resource-tree-grid').treegrid("getSelected");
    	if(node == null){
    		alert("请选择一个项目!");
    		return;
    	}
    	
    	var dataObj = new Object();
    	var url = "";
    	var msg = "";
    	if(node.type == "group"){
    		msg = "删除分组将会删除改组的所有资源,<br>确认要删除【"+node.name+"】吗?";
    		url = "resource/deleteresourceingroup.do";
    	}else if(node.type == "resource"){
    		msg = "确认要删除资源【"+node.name+"】吗?";
    		url = "resource/deleteresource.do";
    	}
    	dataObj.id = node.id;
    	
    	
    	$.messager.confirm('删除', msg, function(r){  
            if (r){  
            	$.loading("正在删除资源...");
            	$.ajax( {
                    "dataType": 'JSON',
                    "type": "POST",
                    "url": url,
                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                    "data": dataObj,
                    "success": function(data){
                    	$.unloading();
                    	 if(data.status == "success"){
                    		 selectedId = null;
                    		 $('#resource-tree-grid').treegrid("reload");
        	          	  }else{
        	          	  }
                    	 $.messaging("删除资源成功!",true);
                    },
                    "error":function(data){
                    	$.unloading();
    		        	$.messager.alert('错误','删除资源失败!，错误原因:<br>'+data.statusText,'error');
        			}
                  } );
            }  
        });  
    	
    }
    
    function getResourceFormContent(formId){
    	var $content = $("<form>").addClass("puma-form").attr("id",formId).attr("method","POST");
		
		$content.append($.getFormItem("资源名称",$("<input type='text' name='name' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入资源名称!'")));
		$content.append($.getFormItem("所属分组",$("<input id='resource-group' name='groupid'>").after($("<a id='reload-group-combobox-btn-id' href='javascript:void(0)' class='easyui-linkbutton'></a>").attr("data-options","iconCls:'icon-refresh',plain:true")).after("不选择分组代表不分组")));
		$content.append($.getFormItem("资源地址",$("<input type='text' name='resourceString' style='width:90%' class='easyui-validatebox'>").attr("data-options","required:true,missingMessage:'请输入资源地址!'")));
		//$content.append($.getFormItem("是否启用",$("<input type='checkbox' name='active' checked='true'>")));
		//$content.append($.getFormItem("是否后台权限",$("<input type='checkbox' name='sys' checked='true'>")));
		$content.append($.getFormItem("描述",$("<textarea name='description' style='width:90%;'>")));
		$content.append($("<input name='id' type='hidden'>"));
		
		return $content;
    }
    
    function viewResourceHandler(row){
    	var windowId = "view-resource-window-id";
    	var formId = "view-resource-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getResourceFormContent(formId);
    		
    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
    			title:"编辑资源",
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
						
						$.loading("正在保存资源...");
    					
    					var url = "resource/update.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
    								$.messaging("编辑资源成功!",true);
    								$('#resource-tree-grid').treegrid('reload');
    								$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','编辑资源发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','编辑资源发生错误，错误原因:<br>'+data.statusText,'error');  
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
    			    $('#resource-group').combobox({  
    			        url:'resourcegroup/combodata.json',  
    			        required:true,
    			        valueField:'id',  
    			        textField:'name',
    			        onSelect:onSelectGroupCombobox
    			    });  
    			    $('#reload-group-combobox-btn-id').bind('click', function(){  
    			    	reloadGroupCombobox();
    			    }); 
    			}
        	});
    	}else{
    		$("#"+windowId).window("center");
    	}
    	//console.log(row);
    	//加载所选择的数据
    	$form = $("#"+formId);
    	$('input[name="id"]', $form).val(row.id);
    	$('input[name="name"]', $form).val(row.name);
    	$('input[name="resourceString"]', $form).val(row.url);
    	$('textarea[name="description"]', $form).val(row.description);
    	//$('input[name="sys"]', $form).attr("checked",row.sys);
    	//$('input[name="active"]', $form).attr("checked",row.active);
    	$('#resource-group').combobox('select',row.groupid);  
    	
    };
    
    function addResourceHandler(){
    	var windowId = "add-new-resource-window-id";
    	var formId = "add-new-resource-form-id";
    	if($("#"+windowId).length == 0){
    		var $content = getResourceFormContent(formId);
    		
    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
    			title:"新增资源",
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
						
						$.loading("正在添加资源...");
    					
    					var url = "resource/create.do";
    					var dataObj = $("#"+formId).formSerialize();
    					$.ajax( {
    	                    "dataType": 'JSON',
    	                    "type": "POST",
    	                    "url": url,
    	                    "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	                    "data": dataObj,
    	                    "success": function(data){
    	                    	if(data.status == "success"){
    								$.messaging("新增资源成功!",true);
    								$('#resource-tree-grid').treegrid('reload');
    								$("#"+windowId).window("destroy");
								}else{
									$.messager.alert('错误','新增资源发生错误，错误原因:<br>'+data.message,'error');  
								}
								$.unloading();
    	                    },
    	                    "error":function(data){
    	                    	$.unloading();
    	                    	$.messager.alert('错误','新增资源发生错误，错误原因:<br>'+data.message,'error');  
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
    			    $('#resource-group').combobox({  
    			        url:'resourcegroup/combodata.json',  
    			        required:true,
    			        valueField:'id',  
    			        textField:'name',
    			        onSelect:onSelectGroupCombobox
    			    });  
    			    $('#reload-group-combobox-btn-id').bind('click', function(){  
    			    	reloadGroupCombobox();
    			    }); 
    			}
        	});  
    	}else{
    		$("#"+windowId).window("center");
    	}
    };
    
    function reloadGroupCombobox(){
    	$("#resource-group").combobox("reload");
    }
    
    function editGroupname(oldname, groupid){
    	$.messager.prompt('编辑组名', '原名称为 【'+oldname+'】 , 请输入新名称', function(r){  
            if (r){  
            	$.loading("正在修改组名...");
            	var dataObj = new Object();
            	dataObj.id = groupid;
            	dataObj.name = r;
            	$.ajax( {
    	            "dataType": 'JSON',
    	            "type": "POST",
    	            "url": "resourcegroup/updatename.do",
    	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
    	            "data": dataObj,
    	            "success": function(data){
    	            	console.log(data);
    	            	$.unloading();
    	            	 if(data.status == "success"){
    	            		 $('#resource-tree-grid').treegrid('reload');
    		          	  }else{
    		          	  }
    	            	 $.messaging("编辑组名成功!",true);
    	            },
    	            "error":function(data){
    	            	$.unloading();
                    	$.messager.alert('错误','编辑组名失败，错误原因:<br>'+data.statusText,'error');  
    				}
    	          } );
            }  
        }); 
    }
    
    function onSelectGroupCombobox(record){
    	//console.log(record);
    	//console.log(this);
    	if(record.id == "create-group"){
    		$(this).combobox("unselect",record.id);
    		var $combo = $(this);
    		$.messager.prompt('新建分组', '请输入新创建组名称', function(r){  
                if (r){  
                    //alert('you type: '+r);
                	$.ajax( {
        	            "dataType": 'JSON',
        	            "type": "POST",
        	            "url": "resourcegroup/createnew.do",
        	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
        	            "data": "name="+r,
        	            "success": function(data){
        	            	 if(data.status == "success"){
        	            		 var groupId = data.message;
        	            		 $combo.combobox("reload");
        	            		 $combo.combobox("setValue",groupId);
        		          	  }else{
        		          	  }
        	            },
        	            "error":function(data){
        	            	$.unloading();
                        	$.messager.alert('错误','新建分组失败，错误原因:<br>'+data.statusText,'error');  
        				}
        	          } );
                }  
            });  
    	}
    }
    
    function enableDnd(t){
    	var nodes = t.treegrid('getPanel').find('tr[node-id]');
    	nodes.find('span.tree-hit').bind('mousedown.treegrid',function(){
    		return false;
    	});
    	nodes.draggable({
    		disabled:false,
    		revert:true,
    		cursor:'pointer',
    		proxy: function(source){
    			var p = $('<div class="tree-node-proxy tree-dnd-no"></div>').appendTo('body');
    			p.html($(source).find('.tree-title').html());
    			p.hide();
    			return p;
    		},
    		deltaX: 15,
    		deltaY: 15,
    		onBeforeDrag:function(){
    			$(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({accept:'no-accept'});
    		},
    		onStartDrag:function(){
    			$(this).draggable('proxy').css({
    				left:-10000,
    				top:-10000
    			});
    		},
    		onDrag:function(e){
    			$(this).draggable('proxy').show();
    			this.pageY = e.pageY;
    		},
    		onStopDrag:function(){
    			$(this).next('tr.treegrid-tr-tree').find('tr[node-id]').droppable({accept:'tr[node-id]'});
    			$(this).draggable('proxy').hide();
    		}
    	}).droppable({
    		accept:'tr[node-id]',
    		onDragOver:function(e,source){
    			var pageY = source.pageY;
    			var top = $(this).offset().top;
    			var bottom = top + $(this).outerHeight();
    			$(this).removeClass('row-append row-top row-bottom');
    			if (pageY > top + (bottom - top) / 2){
    				if (bottom - pageY < 5){
    					$(source).draggable('proxy').removeClass('tree-dnd-no tree-dnd-yes').addClass('tree-dnd-yes');
    					$(this).addClass('row-bottom');
    				} else {
    					$(source).draggable('proxy').removeClass('tree-dnd-no tree-dnd-yes').addClass('tree-dnd-no');
    					//$(this).addClass('row-append');
    				}
    			} else {
    				if (pageY - top < 5){
    					$(source).draggable('proxy').removeClass('tree-dnd-no tree-dnd-yes').addClass('tree-dnd-yes');
    					$(this).addClass('row-top');
    				} else {
    					$(source).draggable('proxy').removeClass('tree-dnd-no tree-dnd-yes').addClass('tree-dnd-no');
    					//$(this).addClass('row-append');
    				}
    			}
    		},
    		onDragLeave:function(e,source){
    			$(source).draggable('proxy').removeClass('tree-dnd-yes').addClass('tree-dnd-no');
    			$(this).removeClass('row-append row-top row-bottom');
    		},
    		onDrop:function(e,source){
    			var action,point;
    			if ($(this).hasClass('row-append')){
    				action = 'append';
    			} else {
    				action = 'insert';
    				point = $(this).hasClass('row-top') ? 'top' : 'bottom';
    			}
    			$(this).removeClass('row-append row-top row-bottom');
    			alert(action+":"+point);
    			// your logic code here
    			// do append or insert action and reload the treegrid data
    		}
    	});
    }
    
})();
