(function(){
	MenuManagementModule = {
			contenturl : "menumanagement.html",
			closable:false,
			init:init
	};
	
	function init(){
		initButtons();
		
		var isDraggingLeftTreeMenu = true;
		var moduleContainerId = "module-selector-content-id";
		var tabid = "tab-menu-tree-id";
		var selectedTabId = 0;
		var textBeforeEdit = "";
		
		initMenuTab();
		
		loadAllMenuTabs();
		
		//init delete panel status;
		$("#delete-drop-area").hide();
		
		function getSelectedTreeId(){
			var $tab = getSelectedTab();
			var selectedTreeId = $tab.attr("id").replace("tabid-", "");
			return selectedTreeId;
		}
		
		function getSelectedTab(){
			var $tab = getTabContainer().tabs("getSelected");
			return $tab;
		}
		
		function getTabContainer(){
			return $("#"+tabid);
		}
		
		function initMenuTab(){
			getTabContainer().tabs({
				onBeforeClose:function(title,index){
					//var target = this;
					var b = false;
					//TODO at lease have one menu
					if(getTabsLength() == 1){
						alert("至少保留一个菜单!");
						return b;
					}else{
						//var index = $(this).parent().parent().index();
						getTabContainer().tabs('select',index);
						deleteMenuTab(index);
					}
					return b;
				}
			});
		}
		
		function initButtons(){
			$("#manage-menu-tab-btn-id").splitbutton({
			    iconCls: 'icon-edit',  
			    menu: '#manage-menu-tab-btn-split-menu-id'  
			});
			$("#manage-menu-tab-btn-id").bind("click",function(){
				addNewMenu();
			});
			$("#add-menu-tab-btn-id").bind("click",function(){
				addNewMenu();
			});
			$("#edit-menu-tab-btn-id").bind("click",function(){
				updateSelectedMenu();
			});
			$("#delete-menu-tab-btn-id").bind("click",function(){
				deleteMenuTab();
			});
			
			
			$("#add-custom-menu-btn-id").bind('click', function(){  
				openAddCustomMenuDialog();
		    });  
			$("#add-module-btn-split-menu-addmodule-id").bind('click', function(){  
		        alert('您可以从右边直接拖动模块到菜单！');  
		    });
			$("#add-module-btn-split-menu-addcustom-id").bind('click', function(){  
				openAddCustomMenuDialog();
		    });
			$("#add-custom-menu-btn-id").splitbutton({
			    iconCls: 'icon-edit',  
			    menu: '#add-module-btn-split-menu-id'  
			});
			
			$("#add-module-group-btn-split-menu-addmodule-id").bind('click', function(){  
				addNewGroup();
		    });
			
			$("#delete-menu-btn-id").bind('click', function(){  
		        alert('您可以把要删除的菜单项拖动到右边区域删除！');  
		    });
			
			$("#refresh-menu-btn-id").bind('click', function(){  
				reloadTree(getSelectedTreeId());
		    });
			
			$("#menu-management-refresh-resrouces-btn-id").bind('click', function(){
				reloadPluginTree();
		    });
		}
		
		function deleteMenuTab(){
			var tab = getSelectedTab();
			var tabid = $(tab).attr("id").replace("tabid-","");
			
			if(tabid === "00000000000000000000000000000000"){
				alert("不能删除首页标签!");
				return;
			}
			var index = getTabContainer().tabs('getTabIndex',tab);
			
			var idArr = getValidPluginInSelectedTree();
			var title = tab.panel("options").title;
			var msg = "您确定要删除标签【"+title+"】吗?";
			if(idArr.length > 0){
				msg = "您删除的标签下有"+idArr.length+"个已经关联的模块菜单项，确定要删除该标签吗?";
			}
			$.messager.confirm('删除标签', msg, function(r){  
	            if (r){  
	            	var opts = getTabContainer().tabs('options');
	            	var bc = opts.onBeforeClose;
	            	
	            	if(removePluginRelations(idArr)){
	            		var menuid = getSelectedTreeId();
	            		if(removeTab(menuid)){
		            		reloadPluginTree();
		            		opts.onBeforeClose = function(){}; // allowed to close now
		            		
		            		getTabContainer().tabs('close',index);
		            		
		            		opts.onBeforeClose = bc;  // restore the event function
	            		}
					}else{
						$.messaging("移除标签失败!");
						b = false;
					}
	            }  
	        });  
		}
		
		function getTabsLength(){
			var tabs = getTabContainer().tabs("tabs");
			return tabs.length;
		}
		
		
		function updateSelectedMenu(){
			var tab = getSelectedTab();
			var selectedTabTitle = tab.panel("options").title;
			var tabid = $(tab).attr("id").replace("tabid-","");
			
			if(tabid === "00000000000000000000000000000000"){
				alert("不能编辑首页标签名称!");
				return;
			}
			
	    	$.messager.prompt('页签名称', '您正在修改标签【'+selectedTabTitle+'】,请输入新的页签名称', function(r){  
	            if (r){  
	            	$.loading("正在新建菜单页签...");
	            	var dataObj = new Object();
	            	dataObj.title = r;
	            	dataObj.id = tabid;
	            	$.ajax( {
	    	            "dataType": 'JSON',
	    	            "type": "POST",
	    	            "url": "menu/updatetabtitle.do",
	    	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	    	            "data": dataObj,
	    	            "success": function(data){
	    	            	$.unloading();
	    	            	 if(data.status == "success"){
	    	            		 $.messaging("修改标签成功!",true);
	    	            		 getTabContainer().tabs("update",{tab: tab,options:{title:r}});
	    		          	  }else{
	    		          	  }
	    	            },
	    	            "error":function(data){
	    	            	$.unloading();
	                    	$.messager.alert('错误','修改标签失败，错误原因:<br>'+data.statusText,'error');
	    				}
	    	          } );
	            }  
	        }); 
		}
		
		function addNewMenu(){
	    	$.messager.prompt('标签名称', '请输入新建标签名称', function(r){  
	            if (r){  
	            	$.loading("正在新建菜单标签...");
	            	var dataObj = new Object();
	            	dataObj.name = r;
	            	$.ajax( {
	    	            "dataType": 'JSON',
	    	            "type": "POST",
	    	            "url": "menu/addnewtab.do",
	    	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	    	            "data": dataObj,
	    	            "success": function(data){
	    	            	$.unloading();
	    	            	 if(data.status == "success"){
	    	            		 var id = data.message.id;
	    	            		 var name = data.message.name;
	    	            		 var data2 = eval(data.message.menuJson);
	    	            		 var isDefault = data.message["default"];
	    	            		 addNewTab(id, name, isDefault, data2, true);
	    		          	  }else{
	    		          	  }
	    	            	 $.messaging("新增标签成功!",true);
	    	            },
	    	            "error":function(data){
	    	            	$.unloading();
	                    	$.messager.alert('错误','新增标签失败，错误原因:<br>'+data.statusText,'error');
	    				}
	    	          } );
	            }  
	        }); 
		}
		
		function lockHandler(){
			var index = $(this).parent().parent().index();
			getTabContainer().tabs('select',index);
			var tab = getSelectedTab();
			var tools = [{  
		        iconCls:'icon-lock',
		        handler:unLockHandler
		    }];
			
			if(getValidPluginInSelectedTree().length == 0){
				alert("要置顶的菜单栏目必须至少有一个系统菜单项，请检查是否只有自定义菜单!");
				return;
			}
			
			
			var menuId = getSelectedTreeId();
			
			if(updateIsDefaultMenu(menuId,true)){
				getTabContainer().tabs("update",{tab: tab,options:{tools:tools,closable:false}});
			}
		}
		
		function unLockHandler(){
			var index = $(this).parent().parent().index();
			getTabContainer().tabs('select',index);
			var tab = getSelectedTab();
			var tools = [{  
		        iconCls:'icon-unlock',
		        handler:lockHandler
		    }];
			
			if(getValidPluginInSelectedTree().length == 0){
				alert("要置顶的菜单栏目必须至少有一个系统菜单项，请检查是否只有自定义菜单!");
				return;
			}
			
			var menuId = getSelectedTreeId();
			if(updateIsDefaultMenu(menuId,false)){
				getTabContainer().tabs("update",{tab: tab,options:{tools:tools,closable:true}});
			}
		}
		
		function getValidPluginInSelectedTree(){
			var arr = new Array();
			
			var treeid = getSelectedTreeId();
			var rootArr = $("#"+treeid).tree("getRoots");
	    	$.each(rootArr,function(i,n){
	    		//console.log($("#"+treeid).tree("getData",n.target));
	    		var data = $("#"+treeid).tree("getData",n.target);
	    		if(data != undefined){
    				if(data.attributes != undefined){
	    				if(data.attributes.type != undefined){
	    					if(data.attributes.type == "folder"){
				    			if(data.children != undefined){
				    				$.each(data.children,function(i,n){
				    					if(this.id != undefined && this.id.length > 0){
				    						if(this.attributes != undefined){
					    	    				if(this.attributes.type != undefined){
					    	    					if(this.attributes.type == "plugin"){
					    	    						if(data.iconCls === "icon-customized-link" || this.iconCls === "icon-customized-module-link"){
					    				    			}else{
					    				    				arr.push(this.id);
					    				    			}
					    	    					}
					    	    				}
					    					}
				    					}
				    				});
				    			}
				    		}else if(data.attributes.type == "plugin"){
				    			if(data.iconCls === "icon-customized-link" || data.iconCls === "icon-customized-module-link"){
				    			}else{
				    				arr.push(data.id);
				    			}
				    		}
			    		}
		    		}
	    		}
	    	});
	    	return arr;
		}
		
		/*function getTheFirstPluginHashInTree(){
			var treeid = getSelectedTreeId();
			var rootArr = $("#"+treeid).tree("getRoots");
			var hash = null;
	    	$.each(rootArr,function(i,n){
	    		if(hash!=null){
	    			return false;
	    		}
	    		//console.log($("#"+treeid).tree("getData",n.target));
	    		var data = $("#"+treeid).tree("getData",n.target);
	    		if(data != undefined){
    				if(data.attributes != undefined){
	    				if(data.attributes.type != undefined){
	    					if(data.attributes.type == "folder"){
				    			if(data.children != undefined){
				    				$.each(data.children,function(i,n){
				    					if(this.id != undefined && this.id.length > 0){
				    						if(this.attributes != undefined){
					    	    				if(this.attributes.type != undefined){
					    	    					if(this.attributes.type == "plugin"){
					    	    						console.log(this.attributes.hash);
					    	    						hash =  this.attributes.hash;
					    	    						return false;
					    	    					}
					    	    				}
					    					}
				    					}
				    				});
				    			}
				    		}else if(data.attributes.type == "plugin"){
				    			hash = data.attributes.hash;
				    			return false;
				    		}
			    		}
		    		}
	    		}
	    	});
	    	return hash;
		}*/
		
		function updateIsDefaultMenu(menuId,status){
			var dataObj = new Object();
			dataObj.menuid = menuId;
			dataObj.status = status;
			var a = false;
			var msg = status?"菜单置顶":"菜单取消置顶";
			$.ajax( {
	            "dataType": 'JSON',
	            "type": "POST",
	            "async":false,
	            "data":dataObj,
	            "url": "menu/updatemenudefaultstatus.do",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": dataObj,
	            "success": function(data){
	            	$.unloading();
	            	 if(data.status == "success"){
	            		 a = true;
	            		 $.messaging(msg+"成功!",true);
		          	  }else{
		          		$.messaging(msg+"失败!");
		          	  }
	            },
	            "error":function(data){
	            	$.unloading();
                	$.messager.alert('错误',msg+'失败，错误原因:<br>'+data.statusText,'error');
				}
	          } );
			return a;
		}
		
		function addNewTab(menuId, name, isDefault, data, select){
			var isMainTab = false;
			//means if it is the main menu
			if(menuId == "00000000000000000000000000000000"){
				isMainTab = false;
			}
			var tools = [];
			if(menuId != "00000000000000000000000000000000"){
				if(!isDefault){
					tools = [{  
				        iconCls:'icon-unlock',  
				        handler: lockHandler
				    }];
				}else{
					tools = [{  
				        iconCls:'icon-lock',  
				        handler: unLockHandler
				    }];
				}
			}
			getTabContainer().tabs("add",{
				id:"tabid-"+menuId,
	    	    title:name,  
	    	    closable:isMainTab?false:!isDefault,
	    	    selected:true,
	    	    tools:tools,
	    	    content:"<ul id='"+menuId+"'></ul>",
	    	    onOpen:function(){
	    	    	setTimeout(function(){consturctTree(menuId, data);},500);
	    	    	$("#"+menuId).click( function(node){  
	    	    		if($(node.target).hasClass("tree-title")){
	    	    			$(this).tree('beginEdit',$(node.target).parent()[0]);  
	    	    		}else if($(node.target).hasClass("tree-node")){
	    	    			return;
	    	    			//$(this).tree('beginEdit',$(node.target)[0]);  
	    	    		}
	                });
	    	    }
			});
			/*if(select != undefined){
				if(select){
					$("#"+tabid).tabs("select",name);
				}
			}*/
			
			getTabContainer().find("ul.tabs").sortable({ cursor: "move", axis: "x", start: function(event, ui){
	    		$(ui.item).children().css("cursor","move");
	    		
	    		var tab = getSelectedTab();
	    		selectedTabId = tab.attr("id");
	    	},stop:function(event, ui){
	    		$(ui.item).children().css("cursor","pointer");
	    		
	    		if(getTabsLength() == 1){return;}
	    		
	    		var $tabHeader = getTabContainer().find("div.tabs-header");
	    		var $tabs = $tabHeader.find("ul.tabs");
	    		var $tabsChildren = $tabs.children("li");
	    		var arr = new Array();
	    		$tabsChildren.each(function(){
	    			arr.push($(this).attr("tab-id").replace("tabid-",""));
	    		});
	    		if(arr.length == 0){
	    			return;
	    		}
	    		var dataObj = new Object();
	    		dataObj.ids = arr;
	    		saveTabOrder(dataObj);
	    	} });
		}
		
		function saveTabOrder(dataObj){
			$.ajax( {
	            "dataType": 'JSON',
	            "type": "POST",
	            "data":dataObj,
	            "url": "menu/savetaborder.do",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": dataObj,
	            "success": function(data){
	            	$.unloading();
	            	 if(data.status == "success"){
	            		 $.messaging("页签排序成功!",true);
		          	  }else{
		          		$.messaging("页签排序失败!",true);
		          		loadAllMenuTabs();
		          	  }
	            },
	            "error":function(data){
	            	$.unloading();
                	$.messager.alert('错误','页签排序失败!，错误原因:<br>'+data.statusText,'error');
                	loadAllMenuTabs();
				}
	          } );
		}
		
		function selectTabById(tabId){
			var i = 0;
			$(getTabContainer().tabs("tabs")).each(function(){
				if($(this).attr("id") == tabId){
					getTabContainer().tabs("select",i);
				}
				i++;
			});
		}
		
		function loadAllMenuTabs(){
			var length = getTabsLength();
			if(length > 0){
				for(var i = length-1; i >= 0; i--){
					getTabContainer().tabs("close", i);
				}
			}
			_loadAllMenuTabsInner();
		}
		
		function _loadAllMenuTabsInner(){
			$.ajax( {
	            "dataType": 'JSON',
	            "type": "POST",
	            "asyn":false,
	            "url": "menu/getallmenutabs.json",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "success": function(data){
	            	if(data.status == "success"){
	            		
	            		var menus = data.message;
	            		if(menus.length > 0){
	            			$(menus).each(function(){
	            				var menuId = this.id;
	            				var name = this.name;
	            				var isDefault = this.isDefault;
	            				var data = this.data;
	            				addNewTab(menuId, name, isDefault, data);
	            			});
	            			//just for the first loading
	            			if(selectedTabId == 0){
	            				getTabContainer().tabs("select",selectedTabId);
	            			}else{
	            				selectTabById(selectedTabId);
	            			}
	            		}
	            	}
	            	
	            	$.unloading();
	            	 $.messaging("加载系统菜单标签成功!",true);
	            },
	            "error":function(data){
	            	$.unloading();
                	$.messager.alert('错误','加载系统菜单标签失败，请刷新本页面重试!，错误原因:<br>'+data.statusText,'error');
				}
	          } );
		}
		function reloadPluginTree(){
			$("#"+moduleContainerId).tree("reload");
		}
		
		//solve issue: Uncaught TypeError: Converting circular structure to JSON !important 
		function nullCircularConverting(data){
			if(data.__proto__ != undefined){
				delete data.__proto__;
			}
			if(data.target != undefined){
				delete data.target;
			}
			if(data.children != undefined){
				$(data.children).each(function(){
					nullCircularConverting(this);
				});
				if(data.children.length == 0){
					delete data.children;
				}
			}
			
			return JSON.stringify(data);
		}
		
		function testSupportJSON(){
			var b = false;
			try{
				JSON.stringify("[]");
				b = true;
			}catch(e){
				console.error(e);
				alert("解析JSON对象失败！如果您使用的是IE6或者IE7，您可能不能使用菜单管理功能，老的浏览器不支持JSON.stringify.建议您更新您的浏览器。详情见：http://caniuse.com/json & http://www.w3help.org/zh-cn/causes/SJ9012");
			}
			return b;
		}
		
		function saveTreeMenu(treeid, isSameTree){
			if(!testSupportJSON()){
				location.href = location.href+" ";
				return false;
			}
				
			var isSaved = false;
			var selectedTabTitle = getSelectedTab().panel("options").title;
			//TODO just save tree menu order and then refresh right module side.
			var rootArr = $("#"+treeid).tree("getRoots");
			var arr = new Array();
	    	$.each(rootArr,function(i,n){
	    		//console.log($("#"+treeid).tree("getData",n.target));
	    		var data = $("#"+treeid).tree("getData",n.target);
	    		var dataStr = nullCircularConverting(data);
	    		if(dataStr !=null){
	    			arr.push(dataStr);
	    		}else{
	    			return false;
	    		}
	    		//console.log(dataStr);
	    	});
	    	var obj = new Object();
    		obj.id = treeid;
    		obj.name = selectedTabTitle;
    		obj.menu = arr;
    		
    		isSaved = saveTreeAjax(obj, treeid);
	    	
	    	if(!isSameTree){
	    		reloadPluginTree();
	    	}
	    	
	    	return isSaved;
		}
		
		function reloadTree(treeid){
			$.ajax( {
                "dataType": 'JSON',
                "type": "POST",
                "url": "menu/getmenutreedata.do?id="+treeid,
                "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                "success": function(data){
                	$("#"+treeid).tree("loadData",eval(data));
                	$.unloading();
                	 //$.messaging("加载菜单成功!",true);
                },
                "error":function(data){
                	$.unloading();
                	$.messager.alert('错误','加载菜单失败!，错误原因:<br>'+data.statusText,'error');
    			}
              } );
	    	
		}
		
		function saveTreeAjax(obj, treeid){
			var b = false;
			$.loading("正在修改菜单...");
			$.ajax( {
                "dataType": 'JSON',
                "type": "POST",
                "async":false,
                "url": "menu/savemenutree.json",
                "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
                "data": obj,
                "success": function(data){
                	b = true;
                	$("#"+treeid).tree("loadData",data.message);
                	
                	$.unloading();
                	 $.messaging("修改菜单成功!",true);
                },
                "error":function(data){
                	reloadTree(treeid);
                	reloadPluginTree();
                	$.unloading();
                	$.messager.alert('错误','修改菜单失败!，错误原因:<br>'+data.statusText,'error');
    			}
              } );
			
			return b;
		}
		
		function relatedPluginToMenutree(treeid, pluginid){
			var b = false;
			var dataObj = new Object();
			dataObj.treeid = treeid;
			dataObj.pluginid = pluginid;
			$.ajax( {
	            "dataType": 'JSON',
	            "type": "POST",
	            "async":false,
	            "url": "plugin/savepluginrelation.do",
	            "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	            "data": dataObj,
	            "success": function(data){
	            	$.unloading();
	            	 if(data.status == "success"){
	            		 b = true;
		          	  }else{
		          		$.messaging("保存模块关联失败!从菜单树中删除刚添加的模块，再重新添加！");
		          	  }
	            },
	            "error":function(data){
                	$.messager.alert('错误','保存模块关联失败!从菜单树中删除刚添加的模块，再重新添加!，错误原因:<br>'+data.statusText,'error');
				}
	          } );
			return b;
		}
		
		function consturctTree(treeid, data){
			if($("#"+treeid).hasClass("tree")){
				return;
			}
			$("#"+treeid).tree({  
			    dnd:true,
			    data:data,
			    onLoadSuccess:function(){
			    	
			    },
			    onDrop:function(target, source, point){
			    	//var targetData = $("#"+treeid).tree("getData",target);
			    	//console.log(targetData);
			    	//var b = saveTreeMenu(treeid, isDraggingLeftTreeMenu);
			    	var b = true;
			    	if(!isDraggingLeftTreeMenu){
			    		var sourceDate = $("#"+moduleContainerId).tree("getData",source);
			    		var sourceNode = sourceDate.target;
			    		var pluginId = sourceNode.id;
			    			//did not setup before
			    		var newAddedNode = $(this).tree("find",pluginId);
			    		newAddedNode.attributes.menuid = treeid;
			    		
			    		//var isUrlMenu =  source.iconCls === "icon-customized-module-link";
						var isRelatedModule = source.iconCls === "icon-customized-module-link";
						if(isRelatedModule){
							source.iconCls = "icon-customized-module-link-in-tree";
							b = true;
						}else{
							//means custom default folder
				    		if(pluginId != "easyui_tree_node_id_temp"){
				    			b = relatedPluginToMenutree(treeid, pluginId);
				    		}
						}
			    	}
			    	if(b){
			    		saveTreeMenu(treeid, isDraggingLeftTreeMenu);
			    	}
			    },
			    onStartDrag:function(node){
			    	isDraggingLeftTreeMenu = true;
		    		showDeleteArea();
		    		$("#delete-drop-area").addClass("delete-active-drop");
			    	$("#delete-drop-inner-area").addClass("delete-active-inner-drop");
			    },
			    onStopDrag:function(node){
			    	showModuleArea();
			    	$("#delete-drop-area").removeClass("delete-active-drop");
			    	$("#delete-drop-inner-area").removeClass("delete-active-inner-drop");
			    },
			    onDragEnter:function(){
			    	$("#tab-menu-tree-id").find(".tree").removeClass("delete-active-drop");
			    },
			    onDragLeave:function(){
			    },
			    onDragOver:function(target, source){
			    	var sourceData = $(this).tree("getData",source.target);
			    	var sourceType = sourceData.attributes.type;
			    	
			    	var targetData = $(this).tree("getData",target);
			    	var targetType = targetData.attributes.type;
			    	
			    	//不允许拖动首页菜单
			    	/*if(targetData.id == "00000000000000000000000000000000" || sourceData.id == "00000000000000000000000000000000"){
			    		if(targetData.id == "00000000000000000000000000000000"){
			    			if($(target).hasClass("tree-node-append") || $(target).hasClass("tree-node-top")){
					    		return false;
					    	}
			    		}
			    	}*/
			    	
			    	//组不允许拖动到组中
			    	if(sourceType == "folder"){
			    		if(targetType == "plugin"){
			    			if($(target).hasClass("tree-node-append")){
					    		return false;
					    	}
			    			return true;
			    		}
			    		if(targetType == "folder"){
			    			if($(target).hasClass("tree-node-append")){
					    		return false;
					    	}
			    		}
			    		return true;
			    	}
			    	
			    	//不允许append到子节点,只允许在其top或者bottom添加
			    	if(sourceType == "plugin"){
			    		if(targetType == "plugin"){
			    			if($(target).hasClass("tree-node-append")){
					    		return false;
					    	}
			    		}
			    		return true;
			    	}
			    },onBeforeEdit:function(node){
			    	$(this).tree('disableDnd');
			    	textBeforeEdit = $.trim(node.text);
			    	//console.log("onBeforeEdit");
			    },
			    onAfterEdit:function(node){
			    	//console.log("onAfterEdit");
			    	$(this).tree('enableDnd');
			    	if($.trim(textBeforeEdit) != $.trim(node.text)){
			    		saveTreeMenu(treeid, true);
			    	}
			    },
			    onCancelEdit:function(node){
			    	//console.log("onCancelEdit");
			    	//saveTreeMenu(treeid, true);
    	    		//$(this).tree('endEdit',node);  
			    }
			});  
		}
		
		function showDeleteArea(){
			$("#"+moduleContainerId).hide();
			$("#delete-drop-area").show();
			$("#delete-drop-area").panel("resize");
		}
		
		function showModuleArea(){
			$("#"+moduleContainerId).show();
			$("#delete-drop-area").hide();
		}
		
		$("#"+moduleContainerId).tree({
		    url:'plugin/unrelatedplugintree.json',
		    dnd:true,
		    onLoadSuccess:function(node, data){
		    	//改变module的图标
		    	$(data).each(function(){
		    		//console.log(this);
		    		//mean default folder node
		    		if(this.id != undefined){
		    			var node = $("#"+moduleContainerId).tree("find",this.id);
			    		if(node.attributes.image != undefined){
			    			if($.trim(node.attributes.image).length > 0){
			    				$(node.target).addClass(node.attributes.image);
			    			}
			    		}
			    		//显示版本等信息的操作
			    		$(node.target).hover(function(){
				    		var $pluginStatusContainer = $("<div class='tree-node-hover plugin-status-container'>");
				    		var $pluginVersion = $("<div class='plugin-status'>").html("版本:"+node.attributes.version);
				    		var $pluginStatus = $("<div class='plugin-status'>").html(node.attributes.menuId);
				    		$pluginStatusContainer.append($pluginVersion).append($pluginStatus);
				    		$(this).parent().append($pluginStatusContainer);
				    	},function(){
				    		$(this).parent().find(".plugin-status-container").remove();
				    	});
		    		}
		    	});
		    },
		    onStartDrag:function(node){
		    	isDraggingLeftTreeMenu = false;
		    	//$(node.target).draggable('proxy').prepend($(node.target).clone());
		    	$("#tab-menu-tree-id").find(".tree").addClass("delete-active-drop");
		    },
		    onStopDrag:function(){
		    	$("#tab-menu-tree-id").find(".delete-active-drop").removeClass("delete-active-drop");
		    },
		    onDrop:function(target, source, point){
		    	//console.log(target);
		    },
		    onDragOver:function(target, source){
		    	//console.log(target);
		    	return false;
		    }
		});  
		
		$("#delete-drop-area").droppable({
			accept:'.tree-node',
			onDragEnter:function(e, source){
				$(".tree-node-proxy span.tree-dnd-icon").removeClass("tree-dnd-yes tree-dnd-no").addClass("tree-dnd-yes");
				$("#delete-drop-area").addClass("delete-accept-drop");
				$("#delete-drop-inner-area").addClass("delete-accept-inner-drop");
			},
			onDragLeave:function(e, source){
				$("#delete-drop-area").removeClass("delete-accept-drop").addClass("delete-active-drop");
				$("#delete-drop-inner-area").removeClass("delete-accept-inner-drop").addClass("delete-active-inner-drop");
			},
			onDrop:function(e, source){
				$("#delete-drop-area").removeClass("delete-accept-drop");
				$("#delete-drop-inner-area").removeClass("delete-accept-inner-drop");
				
				//从左边拖动到右边进行删除
				//use this variable to hack, since this dropping area is to big because of the panel fit:true setting
				if(isDraggingLeftTreeMenu){
					var treeId = $(source).parents("ul.tree").attr("id");
					if(treeId != undefined){
						var pluginId = $(source).attr("node-id");
						var isUrlMenu = $(source).find(".icon-customized-link").length > 0;
						var isRelatedModule = $(source).find(".icon-customized-module-link").length > 0;
						var srouceData = $("#"+treeId).tree("getData", source);
						var isFolder = srouceData.attributes.type === "folder";
						var hasChildren = false;
						if(srouceData.children!=undefined){
							if(srouceData.children.length>0){
								hasChildren = true;
							}
						}
						if(isUrlMenu || isRelatedModule){
							$("#"+treeId).tree("remove", source);
							saveTreeMenu(treeId, false);
						}else if(isFolder){
							if(hasChildren){
								//alert("请先删除该目录下的菜单项后再删除目录!");
								//return false;
								var arr = new Array();
								$.each(srouceData.children,function(){
									if(this.iconCls === "icon-customized-link" || this.iconCls === "icon-customized-module-link"){
									}else{
										if(this.id != null && this.id != undefined){
											arr.push(this.id);
										}
									}
								});
								
								if(removePluginRelations(arr)){
									$("#"+treeId).tree("remove", source);
									saveTreeMenu(treeId, false);
								}
									
							}else{
								$("#"+treeId).tree("remove", source);
								saveTreeMenu(treeId, false);
							}
						}else{
							if(removePluginRelation(pluginId)){
								//need to remove the node from menu tree, and then save it to db
								$("#"+treeId).tree("remove", source);
								saveTreeMenu(treeId, false);
							}
						}
					}
				}
			}
		});
		
		function removeTab(tabid){
			var d = false;
			$.ajax( {
		        "dataType": 'JSON',
		        "type": "POST",
		        "async":false,
		        "url": "menu/removetab.do?id="+tabid,
		        "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
		        "success": function(data){
		        	if(data.status === "success"){
		        		d = true;
		        	}
		        },
		        "error":function(data){
		        	$.unloading();
		        	$.messager.alert('错误','删除标签失败!，错误原因:<br>'+data.statusText,'error');
				}
		      } );
			return d;
		}
		
		function removePluginRelations(idArray){
			if(idArray.length == 0){
				return true;
			}
			var dataObj = new Object();
			dataObj.ids = idArray;
			var d = false;
			$.ajax( {
		        "dataType": 'JSON',
		        "type": "POST",
		        "async":false,
		        "data":dataObj,
		        "url": "plugin/removepluginrelations.do",
		        "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
		        "success": function(data){
		        	//console.log(data.message);
		        	if(data.status === "success"){
		        		d = true;
		        	}
		        },
		        "error":function(data){
		        	$.unloading();
		        	$.messager.alert('错误','删除模块关联失败!，错误原因:<br>'+data.statusText,'error');
				}
		      } );
			return d;
		}
		
		function removePluginRelation(pluginid){
			var d = false;
			$.ajax( {
		        "dataType": 'JSON',
		        "type": "POST",
		        "async":false,
		        "url": "plugin/removepluginrelation.do?id="+pluginid,
		        "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
		        "success": function(data){
		        	if(data.status === "success"){
		        		d = true;
		        	}
		        },
		        "error":function(data){
		        	$.unloading();
		        	$.messager.alert('错误','删除模块关联失败!，错误原因:<br>'+data.statusText,'error');
				}
		      } );
			return d;
		}
		
		function openAddCustomMenuDialog(){
			var windowId = "menumanagement-add-custom-menu-window-id";
	    	if($("#"+windowId).length == 0){
	    		
	    		$("<div>").attr("id",windowId).appendTo("body").dialog({ 
	    			title:"新增自定义菜单",
	        	    width:500,  
	        	    height:320,  
	        	    modal:false,
	        	    cache: true,
	        	    collapsible:false,
	        	    minimizable:false,
	        	    maximizable:false,
	        	    resizable:true,
	        	    href:"menumanegement-addcustommenu.html",
	        	    buttons:[{
	    				text:'保存',
	    				handler:function(){
	    					if(onAddCustomMenu()){
	    						$("#"+windowId).window("destroy");
	    					}
	    				}
	    			},{
	    				text:'取消',
	    				handler:function(){$("#"+windowId).window("destroy");}
	    			}],
	    			onClose:function(){
	    				$(this).window("destroy");
	    			}
	        	});
	    	}else{
	    		$("#"+windowId).window("center");
	    	}
		}
		
		function addCustomUrlMenu(name, url, opentype){
			var selectedTreeId = getSelectedTreeId();
			var newMenuData = {
							    "text": name,
							    "iconCls": "icon-customized-link",
							    "attributes": {
							        "url": url,
							        "opentype":opentype,
							        "type": "plugin"
							    },
							    "checked": false
							};
			var roots = $('#'+selectedTreeId).tree('getRoots');
			
			$('#'+selectedTreeId).tree('insert', {
				before: roots[0].target,
				data: newMenuData
			});
			return saveTreeMenu(selectedTreeId, true);
		}
		
		function addNewGroup(){

	    	$.messager.prompt('组名称名称', '请输入新菜单组名称', function(r){  
	            if (r){  
	    			var selectedTreeId = getSelectedTreeId();
	    			var newMenuData = {
	    							    "text": r,
	    							    "iconCls": "icon-none",
	    							    "attributes": {
	    							        "type": "folder"
	    							    },
	    							    "checked": false,
	    							    "state": "open"
	    							};
	    			var roots = $('#'+selectedTreeId).tree('getRoots');
	    			$('#'+selectedTreeId).tree('insert', {
	    				before: roots[0].target,
	    				data: newMenuData
	    			});
	    			return saveTreeMenu(selectedTreeId, true);
	            }  
	        }); 
		
		}
		
		function addCustomModuleMenu(name, moduleHash){
			var selectedTreeId = getSelectedTreeId();
			var newMenuData = {
							    "text": name,
							    "iconCls": "icon-customized-module-link",
							    "attributes": {
							        "hash": moduleHash,
							        "type": "plugin"
							    },
							    "checked": false
							};
			var roots = $('#'+selectedTreeId).tree('getRoots');
			
			$('#'+selectedTreeId).tree('insert', {
				before: roots[0].target,
				data: newMenuData
			});
			return saveTreeMenu(selectedTreeId, true);
		}
		
		function onAddCustomMenu(){
			var b = false;
			var $menuNameText = $("#menumanagement-addcustommenu-name-input");
			var menuName = $menuNameText.val();
			var $customUrl = $("#menumanagement-addcustommenuurl-address-input");
			var customUrl = $customUrl.val();
			var radioValue = $('#menumanagement-addcustommenu-tab-id input[name="opentype"]').filter(':checked').val();
			var $moduleCombobox = $("#menumanagement-addcustommenuurl-module-combobox");
			var moduleHash = $moduleCombobox.combobox("getValue");

			var isValidCustomUrlForm = function(){
				var isValidName = $menuNameText.validatebox("isValid");
				var isValidUrl = $customUrl.validatebox("isValid");
				return isValidName&&isValidUrl;
			};
			
			var isValidCustomModuleForm = function(){
				var isValidName = $menuNameText.validatebox("isValid");
				var isValidModule = $moduleCombobox.combobox("isValid");
				return isValidName&&isValidModule;
			};
			
			var $tabs = $("#menumanagement-addcustommenu-tab-id");
			var tab = $tabs.tabs('getSelected');
			var index = $tabs.tabs('getTabIndex',tab);
			if(index == 0){
				//url menu
				if(isValidCustomUrlForm()){
					b = addCustomUrlMenu(menuName, customUrl, radioValue);
				}else{
					alert("信息输入不正确!");
				}
			}else if(index == 1){
				//module menu
				if(isValidCustomModuleForm()){
					b = addCustomModuleMenu(menuName,moduleHash);
				}else{
					alert("信息输入不全!");
				}
			}
			return b;
		}
	}
})();
