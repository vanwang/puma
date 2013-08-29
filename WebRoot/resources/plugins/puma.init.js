/**************************************************************************
 * 
 * 					Puma framework init js
 * 
 * 1. Puma Variables declaration
 * 2. Puma framework js API 
 * 3. Puma framework js utils
 * 4. Puma private functions 
 * 5. Puma utils functions
 * 
 * @author wangfan
 * @date 2013-3-23
 * @version 1.0
 * 
 ************************************************************************* */
/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document, undefined){
	/********************************************************/
	/*               1. Puma Variables declaration          */
	/********************************************************/
	//used to store system's available plugins
	var pluginsMap = new Map(),
	
	//used to store system menus
	menusMap = new Map(),
	
	//used to store system preopened plugins
	preOpenedModulesHash = [],
	
	//used to store module's initia parameter object's hash
	//{moduleName+paramObj.toString:random string}
	objHash = new Map(),
	
	//flag used to check if the system is loaded successfully
	systemInitSucc = false,
	
	//used to store messaging timer
	messagingTimer = new Array();
	
	
	/********************************************************/
	/*                2. Puma framework js API              */
	/********************************************************/
	
	/**
	 * $.loadPumaModule方法是用来加载定义的前端组件，如果不用该方法加载，可以使用 @API $.getMainTabContainer得到easyui的tab对象，
	 * 然后利用easyui的方法来添加自己定义的页面。具体方法 @see $.getMainTabContainer()
	 * 
	 * @param Array modulesHash 数组类型 在浏览器中所显示的hash地址，例如：#module=ChangeSkinModule|{"data":"123"}或者#module=ChangeSkinModule|{}.
	 * 					  		需要注意的是，如果参数不同，虽然使用的是同一模块，但是会在不同的tab标签页面打开。
	 * 							必须为数组类型，您可以加载多个模块，如果只想加载一个模块的话，就之往数组里添加一个就可以了。
	 * 							例如["#module=SystemHomeModule|{}"]
	 * @param callback	加载完成后所执行的回调函数，可以不传，也可以设置为null
	 * 
	 * @since 1.0
	 * */
	$.loadPumaModule = function(modulesHash, callback){
		loadPumaModule(modulesHash, callback);
	};
	
	/**
	 * $.getMainTabContainer方法是用来范围主界面easyui的tab对象，用户得到这个tab对象后，可以使用easyui-tab所提供的所有方法
	 * 
	 * @since 1.0
	 * */
	$.getMainTabContainer = function(){
		return $("#main-tab");
	};
	
	/**
	 * $.updateMainTabTitle方法是用来设置主界面中，当前所选择的页签的名称。有些用户需要基于同一个模块，传入不同的参数，从而打开不同的标签页。
	 * 但是如果不更新标签页的名称，只用默认的名称会，众多的标签会让使用者感到迷惑，所以开发人员可以在模块的init方法中，调用该方法，更新标签的名称。
	 * 
	 * @param title	需要设置的名称
	 * 
	 * @since 1.0
	 * */
	$.updateMainTabTitle = function(title){
		updateMainTabTitle(title);
	};
	
	
	/**
	 * $.loading方法是用来在界面的左上角显示加载的信息
	 * 
	 * @param title	需要显示的加载信息
	 * 
	 * @since 1.0
	 * */
	$.loading = function(title) {
		loading(title);
	};
	
	/**
	 * $.unloading方法是去掉在界面的左上角显示加载的信息
	 * 
	 * @since 1.0
	 * */
	$.unloading = function(){
		unloading();
	};
	
	/**
	 * $.messaging方法是用来在界面的正上方显示提示信息
	 * 
	 * 调用方式比较灵活：
	 * 1. $.messaging("hello world");显示hello world 2秒
	 * 2. $.messaging("hello world",true);显示hello world 2秒，背景为绿色
	 * 3. $.messaging("hello world",5000);显示hello world 5秒
	 * 4. $.messaging("hello world",true,5000);显示hello world 5秒， 背景为绿色
	 * 
	 * @param title	需要显示的信息
	 * @param bool success	参数类型为布尔类型，是否显示为成功的样式，即背景为绿色， 默认值为false，意思为普通信息提示。
	 * @param number time	停留时间，默认为2000毫秒
	 * 
	 * @since 1.0
	 * */
	$.messaging = function(title, success, time) {
		messaging(title, success, time);
	};
	
	/**
	 * $.unloading方法是去掉界面正上方的信息提示
	 * 
	 * @since 1.0
	 * */
	$.unmessaging = function(){
		unmessaging();
	};
	
	/**
	 * $.getFormItem方法是用来在表单中添加输入框和标签的时候使用
	 * 
	 * @param label	标签名称
	 * @param content	控件内容，比如可以是个输入框<a class="easyui-linkbutton">按钮</a>
	 * @param containerid	容器的id，也可以不设定
	 * 
	 * 
	 * @since 1.0
	 * */
	$.getFormItem = function(label, content, containerid){
		return getFormItem(label, content, containerid);
	};
	
	/********************************************************/
	/*                3. Puma framework js utils            */
	/********************************************************/
	$.util = {};
	/**
	 * $.isEmptyObj方法是用来判断一个对象是否有值。
	 * 
	 * 比如var obj = {};那么 $.isEmptyObj(obj)返回的值就是true, 
	 * 如果var obj = {"data":"value"},那么返回值就是false
	 * 
	 * @param obj	需要判断的对象
	 * 
	 * @since 1.0
	 * */
	$.util.isEmptyObj = function(obj){
		return isEmptyObj(obj);
	};
	
	/**
	 * $.util.json2str方法是将JSON对象转成字符串
	 * 因为IE7不支持JSON.stringify(json)方法，所以添加该方法
	 * 
	 * @param obj	需要转型的JSON对象
	 * 
	 * @since 1.0
	 * */
	$.util.json2str = function(obj){
		return json2str(obj);
	};
	
	/**
	 * $.generateRandomString方法用来产生随机字符串。
	 * 
	 * @param length	要产生随机串的长度，如果不设置该参数，会自动产生长度为32的随机字符串
	 * 
	 * @since 1.0
	 * */
	$.util.generateRandomString = function(length){ 
	    length = length || 32; 
	    //var source = "abcdefghzklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`0123456789-=//[];',./~!@#$%^&*()_+|{}:/<>?"; 
	    var source = "abcdefghzklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    var s = ""; 
	    for(var i = 0;i < length; i++)  { 
	        s += source.charAt(Math.ceil(Math.random()*1000)%source.length); 
	    }
	    return s; 
	};
	
	
	/********************************************************/
	/*                 4. Puma private functions            */
	/********************************************************/
	function initSystem(){
			/*var i = 0;
			while(i < 50){
				i++;
				console.log($.generateRandomString());
			}
			return;*/
			$.loading("正在初始化系统基础数据...");
		
			//need to initmaintab to add necessary listener
			initMainTab();
			
			$("#open-menu").bind("click",toggleSideNavBar);
			
			if(loadSystemModule()){
				$.loading("正在加载系统菜单...");
				if(loadSystemMenu()){
					initHashChangeListener();
					//do a small hack here to lazy enter system
					setTimeout(enterSystem,500);
					//mean's system is loaded successfully
					systemInitSucc = true;
				}
			}
			if(!systemInitSucc){
				alert("初始化系统失败,请手动刷新后重试！");
				return;
			}
	}
	
	function enterSystem(){
		$.unloading();
		$.messaging("系统初始化成功,进入系统!",true);
		loadPumaModule(preOpenedModulesHash, function(){
				loadPumaModule([$.trim(window.location.hash)], null,false);
		}, true);
	}
	
	function loadSystemModule(){
		var c = false;
		$.ajax( {
	        "dataType": 'JSON',
	        "type": "POST",
	        "async":false,
	        "url": "plugin/getallplugininfo.json",
	        "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	        "success": function(data){
	        	$(data).each(function(){
	        		pluginsMap.put(this.jsModuleName, this);
	        	});
	        	c = true;
	        },
	        "error":function(data){
	        	alert("初始化系统(加载系统模块)失败,请手动刷新后重试！");
	        	return;
			}
	      } );
		return c;
	}
	
	function loadSystemMenu(){
		var b = false;
		$.ajax( {
	        "dataType": 'JSON',
	        "type": "POST",
	        "async":false,
	        "url": "menu/getsystemmenus.json",
	        "contentType": "application/x-www-form-urlencoded; charset=UTF-8",
	        "success": function(data){
	        	if(data.status == "success"){
	        		consturctSystemMenus(data.message);
	        	}
	        	b = true;
	        },
	        "error":function(data){
	        	alert("初始化系统(加载系统菜单)失败,请手动刷新后重试！");
	        	return;
			}
	      } );
		return b;
	}
	
	function toggleSideNavBar(){
		if($(".frame-main-content").css("left") != "0px"){
			$("body").addClass("menu-hidden");
			$("#logo-image-id").attr("src","resources/themes/img/puma-logo-single-no-framework.png");
			//$(".frame-main-nav").css("width",0);
			$(".frame-main-content").animate({ 
				left: "0"
			  }, {duration:200, complete:function(){$(window).trigger("resize");}} );
			//$(".frame-main-content").css("left",0);
			
		}else{
			$("body").removeClass("menu-hidden");
			//$(".frame-main-nav").css("width","200px");
			//$("#frame-main-nav").panel("resize");
			//$(".frame-main-content").css("left","201px");
			$(".frame-main-content").animate({ 
				left: "201px"
			  }, 
			  {duration:200, complete:function(){
				  $(window).trigger("resize");
				  $("#logo-image-id").attr("src","resources/themes/img/puma-logo-single.png");
				 }
			  } );
		}
	}
	
	function getTheFirstPluginHashFromMenu(treeid){
		var rootArr = $("#"+treeid).tree("getRoots");
		var hash = null;
    	$.each(rootArr,function(i,n){
    		if(hash!=null){
    			return false;
    		}
    		var data = $("#"+treeid).tree("getData",n.target);
    		if(data){
    			if(data.attributes){
    				if(data.attributes.type){
    					if(data.attributes.type == "folder"){
			    			if(data.children){
			    				$.each(data.children,function(i,n){
			    					if(this.attributes){
			    	    				if(this.attributes.type){
			    	    					if(this.attributes.type == "plugin"){
			    	    						if(data.iconCls === "icon-customized-link" || this.iconCls === "icon-customized-module-link"){
			    				    			}else{
			    				    				//arr.push(this.id);
			    				    				hash =  this.attributes.hash;
			    				    				return false;
			    				    			}
			    	    					}
			    	    				}
			    					}
			    				});
			    			}
			    		}else if(data.attributes.type == "plugin"){
			    			if(data.iconCls === "icon-customized-link" || this.iconCls === "icon-customized-module-link"){
			    			}else{
			    				hash = data.attributes.hash;
				    			return false;
			    			}
			    		}
		    		}
	    		}
    		}
    	});
    	return hash;
	}
	
	function consturctSystemMenus(data){
    	$(data).each(function(){
    		menusMap.put(this.id, this);
    		$("<ul></ul>").attr("id",this.id).addClass("left-menu-inactive").appendTo($("#frame-main-nav")).tree({  
    			data:this.data,
			    onClick:treeOnClick
			});
    		if(this.isDefault){
    			var hash = getTheFirstPluginHashFromMenu(this.id);
    			if(hash){
    				preOpenedModulesHash.push(hash);
    			}
    		}
    	});
	}
	
	function treeOnClick(node){
		if(node.attributes){
			var attributes = node.attributes;
			if(attributes.hash){
				window.location.hash = attributes.hash;
			}else if(attributes.url){
				var url = attributes.url;
				var opentype = attributes.opentype;
				var text = node.text;
				if(opentype == "window"){
					window.open(url);
				}else if(opentype == "iframe"){
					addNewIframeTab(text, url);
				}
			}
		}else{
			//alert("node do not contain url hash value");
		}
	}
	
	function initHashChangeListener(){
		$(window).hashchange( function(){
			//var mName = getModuleNameFromHash(location.hash);
			loadPumaModule([$.trim(window.location.hash)],null,false);
		});
	}
	
	function gotoHomePage(){
		var homeModuleHash = ["#module=SystemHomeModule|{}"];
		loadPumaModule(homeModuleHash, null,false);
	}
		
	function getModuleNameFromHash(hashString){
		var obj = getParamObjFromHash(hashString);
	    return obj.moduleName;
	}
	
	function getParamobjByModulename(moduleName, hashes){
		for(var i = 0; i < hashes.length; i++){
			var hash = hashes[i];
			var obj = getParamObjFromHash(hash);
			if(obj.moduleName == moduleName){
				return obj.paramObj;
			}
		}
	}
	
	function getParamObjFromHash(hash){
		//example hash: #module=moduleName|{param1:"1",param2:"2"}
		var paramObj = new Object();
		paramObj.moduleName = "SystemHomeModule";
		paramObj.paramObj = null;
		hash = $.trim(hash);
		
		if(hash.substring(0,1) == "#"){
			hash = hash.substring(1, hash.length);
	    }
		if(hash.indexOf("|") == -1){
			return paramObj;
		}
		var arr = hash.split("|");
		var moduleNameString = arr[0];
		var paramString = arr[1];
		
		if(moduleNameString.indexOf("=") != -1){
			var moduleName = moduleNameString.split("=")[1];
			if(moduleName){
				paramObj.moduleName = moduleName;
			}
		}
		
		try {
			eval("var theJsonValue = "+paramString);
			if(isEmptyObj(theJsonValue)){
				paramObj.paramObj = null;
			}else{
				paramObj.paramObj = theJsonValue;
			}
		}catch(e) {
		  console.error(e);
    	  alert("The parameter used to initialize module "+ paramObj.moduleName + ". The passed parameter in hash is not a valid JSON format. Please check it and repass the initialize parameter.");
    	  //gotoHomePage();
    	  //return;
    	}
		return paramObj;
	}
	
	function isEmptyObj(obj){
		var n = 0;
		for(i in obj){
			n++;
			if(n > 0){
				break;
			}
		}
		return n == 0;
	}
	
	function initMainTab(){
		$("#main-tab").tabs({
			fit:true,
			onSelect:onSelect,
			onBeforeClose:onBeforeClose
		});
	}
	
	function initModuleTab(tabid){
		$("#"+tabid).tabs({
			fit:true,
			plain:true,
			tabPosition : 'left',
			headerWidth : 0
		});
		$(window).on('resize', $("#"+tabid), function() {
			if($("#"+tabid).length > 0){
				$("#"+tabid).tabs('resize');
			}
	    });
	}
	
	function addNewIframeTab(title, url){
		$content = $("<iframe>").attr("frameborder","0").attr("marginwidth","0").attr("marginheight","0")//.attr("scrolling","no")
							.attr("width","100%").attr("height","98%").attr("src",url);
		$panel = $("<div>").attr("class","easyui-panel").attr("data-options","fit:true").css("overflow","hidden").append($content);
		var tab = $("#main-tab").tabs("getTab",title);
		if(tab == null){
			$("#main-tab").tabs("add",{
				fit:true,
	    	    title:title,  
	    	    closable:true,
	    	    selected:true,
	    	    content:$panel
			});
		}else{
			$("#main-tab").tabs("select",title);
		}
		
	}
	
	function addNewMainTab(module,closable){
		var c = closable==undefined?true:closable;
		
		var title = module.title;
		if(module.menuid){
			var menuData = menusMap.get(module.menuid);
			if(menuData){
				if(menuData.name){
					title = menuData.name;
				}
			}
		}
		$("#main-tab").tabs("add",{
    		id:module.tabid,
    	    title:title,  
    	    closable:c,
    	    selected:module.selected,
    	    closed:true
		});
	}
	
	function isModuleTabInitialized(tabid){
		return $("#"+tabid+" > .tabs-panels").length > 0;
	}
	
	function addNewModuleTab(module){
			$("#"+module.tabid).tabs('add',{ 
	    		id:module.id,
	    	    title:module.title,  
	    	    href:module.contenturl,
	    	    selected:module.selected,
	    	    closed:true,
	    	    onLoad:function(){
	    	    	if(module.init){
	    	    		if (typeof(module.init)=="function"){
		    	    		module.init.call(this,module.param);
						}
	    	    	}
	    	    	if(module.onresize){
	    	    		$(window).resize(function(){
	    	    			if (typeof(module.onresize)=="function"){
			    	    		module.onresize.call();
							}
	    	    		});
	    	    		
	    	    	}
	    	    }
	    	}); 
	}
	
	function updateMainTabTitle(title){
		var tab = $('#main-tab').tabs('getSelected');  // get selected panel
		$('#main-tab').tabs('update', {
			tab: tab,
			options: {
				title: title
			}
		});
	}
	
	function selectModuleLeftMenu(module){
		var mid = module.id.replace("system-module-","");
		if(!isEmptyObj(module.param)){
			//var uid = objHash.get(module.name + JSON.stringify(module.param));
			//var uid = objHash.get(module.name + json2str(module.param));
			var uid = objHash.get(module.hash);
			
			mid = mid.replace(uid,"");
		}
		var menuid = module.menuid;
		var node =  $('#'+menuid).tree('find', mid);
		if(node){
			$('#'+menuid).tree('select', node.target);
			$("ul.left-menu-active").removeClass("left-menu-active").addClass("left-menu-inactive");
			$("#"+menuid).removeClass("left-menu-inactive").addClass("left-menu-active");
		}else{
			setTimeout(function(){
				node = $('#'+menuid).tree('find', module.id);
				if(node){
					$('#'+menuid).tree('select', node.target);
					$("ul.left-menu-active").removeClass("left-menu-active").addClass("left-menu-inactive");
					$("#"+menuid).removeClass("left-menu-inactive").addClass("left-menu-active");
				}
			},200);
		}
	}
	
	  function onBeforeClose(title,index){
			var tab = $("#main-tab").tabs('getTab',index);
			var hash = $(tab).data("hash");
			if(!hash){
				return;
			}
			var mName = getModuleNameFromHash(hash);
			var module = eval(mName);
			if(module){
				if(module.onclose){
					var test = (module.onclose.apply());
					return test;
				}
			}
			
			var tabid = $("#main-tab > div.tabs-panels > div.panel:nth-child("+(index+1)+") > div").attr("id");
			$(window).off('resize', $("#"+tabid), function() {
		    });
			
			return true;
		  };
	
	 function onSelect(title,index){
		//need to trigger reseize event inorder to resize tabs which not shown
		var tab = $("#main-tab").tabs('getTab',index);
		var hash = $(tab).data("hash");
		if(!hash){
			return;
		}
		window.location.hash = hash;
	};
	
	function loadPumaModule(modulesHash, callback, isFirstloadhas){
		var moduleNames = [];
		for(var i = 0; i < modulesHash.length; i ++){
			var hash = modulesHash[i];
			var n = getModuleNameFromHash(hash);
			moduleNames.push(n);
		}
		easyloader.load(moduleNames, function(){
			var aa = isFirstloadhas;
			for(var i = 0; i < moduleNames.length; i ++){
				var moduleName = moduleNames[i];
				if(modulesHash[i].length == 0 && moduleName === "SystemHomeModule"){
					modulesHash[i] = "#module=SystemHomeModule|{}";
				}
				var obj = getParamobjByModulename(moduleName, modulesHash);
				//var objStr = "";
				if(obj !== null){
					/*try{
						//objStr = JSON.stringify(obj);
						objStr = json2str(obj);
					}catch(e){
						console.error(e);
						alert("The parameter used to initialize module "+ moduleName + " is not a valid JSON format. Please check it and repass the initialize parameter.");
					}*/
					//objHash.put(moduleName+objStr, $.util.generateRandomString());
					objHash.put(modulesHash[i], $.util.generateRandomString());
				}else{
					//obj = new Object();
				}
				//var o = objHash.get(moduleName+objStr);
				var o = objHash.get(modulesHash[i]);
				if(!o){
					o = "";
				}
				try{
					eval("var module = "+moduleName);
					var moduleData = pluginsMap.get(moduleName);
					module.name = moduleName;
					module.tabid = "system-tabid-"+moduleData.menuId+o;
					module.id = "system-module-"+moduleData.id+o;
					module.menuid = "system-"+moduleData.menuId;
					module.title = moduleData.name;
					module.selected = true;
					module.param = obj;
					module.hash = modulesHash[i];
					//module.menuid = moduleData.menuId;
				}catch(e) {
					  console.error(e);
			    	  alert(moduleName+" not loaded or you change the url in your borwser.Will take you to home page!");
			    	  gotoHomePage();
			    	  return;
		    	}
		    	if(!module){
		    		alert("plugin "+moduleName+" not initialized");
		    		return;
		    	}
		    	var title = module.title;
		    	if(title) {
		    		document.title = title + " - PUMA Framework";
			    } else {
			    	document.title = "未命名模块" + " - PUMA Framework";
			    }
		    	
	    		//查找是否已经加载过模块的tab
	    		var isTabed = $("#"+module.tabid).length > 0;
	    		//查找是否已经加载过模块
	    		var isModuled = $("#"+module.id).length > 0;
	    		if(isTabed){
	    			//var tabindex = $("#"+module.tabid).parent().parent().children().index($("#"+module.tabid).parent());
	    			var tabindex = $("#"+module.tabid).parent().index();
	    			
	    			var tab = $("#main-tab").tabs('getTab',tabindex);
	    			//$(tab).data("hash","#module="+moduleName+"|"+json2str(obj));
	    			//$(tab).data("hash","#module="+moduleName+"|"+JSON.stringify(obj));
	    			$(tab).data("hash",module.hash);
	    			$("#main-tab").tabs('silentSelect',tabindex);
	    			
		    		if(isModuled){
		    			/*if(!isModuleTabInitialized(module.tabid)){
		    				initModuleTab(module.tabid); 
		    			}*/
	    				//var moduleindex = $("#"+module.id).parent().parent().children().index($("#"+module.id).parent());
		    			/*var moduleindex = $("#"+module.id).parent().index();
		    			//console.log($("#"+module.tabid).tabs("getSelected"));
		    			//console.log(module.tabid);
		    			console.log($("#"+module.tabid).length);
		    			$("#"+module.tabid).tabs('select',moduleindex);*/
		    		}else{
		    			if(!isModuleTabInitialized(module.tabid)){
		    				initModuleTab(module.tabid); 
		    			}
		    			if(!aa){
		    				module.selected = false;
		    				addNewModuleTab(module);
		    			}
		    		}
	    		}else{
	    			addNewMainTab(module, aa?false:true);
	    			
	    			//var tabindex = $("#"+module.tabid).parent().parent().children().index($("#"+module.tabid).parent());
	    			var tabindex = $("#"+module.tabid).parent().index();
	    			
	    			var tab = $("#main-tab").tabs('getTab',tabindex);
	    			
	    			//$(tab).data("hash","#[{module:'"+moduleName+"'}]");
	    			//$(tab).data("hash","#module="+moduleName+"|"+json2str(obj));
	    			//$(tab).data("hash","#module="+moduleName+"|"+JSON.stringify(obj));
	    			$(tab).data("hash",module.hash);
	    			
	    			$("#main-tab").tabs('silentSelect',tabindex);
	    			
	    			initModuleTab(module.tabid);
	    			if(!aa){
	    				module.selected = false;
	    				addNewModuleTab(module);
	    			}
	    		}
	    		if(!aa){
	    			selectModuleLeftMenu(module);
	    		}
	    		selectInnerTab(module);
	    		/*var moduleindex = $("#"+module.id).parent().index();
    			$("#"+module.tabid).tabs('silentSelect',moduleindex);*/
	    		//updateMainTabTitle(title);
			}
			
			if(callback){
				if (typeof(callback)=="function"){
					callback.call();
				}
			}
			
			$(window).trigger("resize");
		});
	}
	
	function selectInnerTab(module){
		var moduleindex = $("#"+module.id).parent().index();
		$("#"+module.tabid).tabs('select',moduleindex);
	}
	
	
	/********************************************************/
	/*                 5. Puma utils functions              */
	/********************************************************/
	function getFormItem(label, content, containerid){
    	var $itemlabel = $("<div>").addClass("puma-form-label");
		var $itemcontent = $("<div>").addClass("puma-form-content");
		var $item = $("<div>").addClass("puma-form-item");
		if(containerid){
			$item.attr("id",containerid);
		}
		return $item.append($itemlabel.html(label)).append($itemcontent.html(content));
    }
	
	function loading(title) {
		if($("#loading-indicator").length == 0)
		{
			$("<div>").addClass("loading-indicator").attr("id","loading-indicator")
				.append($("<span>").addClass("loading-icon"))
				.append($("<span>").addClass("loading-text"))
				.appendTo("body");
		}
		$("span.loading-text",$("#loading-indicator")).html(title);
		$("#loading-indicator").fadeIn();
	};
	
	function unloading(){
		$("#loading-indicator").fadeOut();
	};
	
	function messaging(title, success, time) {
		if(success && typeof(success) == "number"){
			time = success;
		}
		if(!time){
			time = 2000;
		}
		if($("#messaging-indicator").length == 0)
		{
			$("<div>").attr("id","messaging-indicator")
				.html(title)
				.appendTo("body");
		}
		
		$("#messaging-indicator").removeClass("messaging-indicator-suc").addClass("messaging-indicator-normal");
		
		if(success){
			$("#messaging-indicator").removeClass("messaging-indicator-normal").addClass("messaging-indicator-suc");
		}
		$("#messaging-indicator").html(title);
		var w = $(window).width();
		var w2 = $("#messaging-indicator").width();
		var left = (w-w2)/2;
		$("#messaging-indicator").css("left",left).slideDown("fast");
		
		if(messagingTimer.length>0){
			clearTimeout(messagingTimer[0]);
			messagingTimer.pop();
		}
		var id = setTimeout(function(){
			unmessaging();
			messagingTimer.pop();
		}, time);
		messagingTimer.push(id);
	};
	
	function unmessaging(){
		$("#messaging-indicator").slideUp();
	};
	
	function json2str(o) {
	    var arr = [];
	    var fmt = function(s) {
	        if (typeof s == 'object' && s != null) return json2str(s);
	        return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s;
	     };
	    for (var i in o) arr.push("'" + i + "':" + fmt(o[i]));
	    return '{' + arr.join(',') + '}';
	 }
	
	function json2str2(o) {
	   var arr = [];
	   var fmt = function(s) {
	       if (typeof s == 'object' && s != null) return json2str(s);
	       return /^(string|number)$/.test(typeof s) ? s : s;
	    };
	   for (var i in o) arr.push(i + ":" + fmt(o[i]));
	   return '{' + arr.join(',') + '}';
	}

	//Initializing of the Puma System
	$(document).ready(function() {
		initSystem();
		$(window).resize(function(){
			$("#frame-main-nav").panel('resize');
			$("iframe").attr("height","98%").attr("width","100%");
			if($("#main-tab").length > 0){
				$("#main-tab").tabs("resize");
			}
		});
	});
})(this.jQuery, window, document);

/**
 * 
 * 统一处理ajax事件
 * 
 * */
;(function($){  
    var ajax=$.ajax;  
    $.ajax=function(s){  
    	//console.log(s);
    	//console.log(s.error);
        var old=s.error;  
        var oldSuccess = s.success;
        s.success=function(msg){
        	if(msg != undefined){
        		if(msg.status != undefined && (msg.status == "error" || msg.status == "warning")){
        			$.messaging(msg.message,"false",5000);
        		}
        	}
        	if(oldSuccess != undefined){
        		oldSuccess(msg);
			}
        };
        //var errHeader=s.errorHeader||"Error-Json";  
        s.error=function(XMLHttpRequest, textStatus, errorThrown){  
            //var errMsg = window["eval"]("(" + xhr.getResponseHeader(errHeader) + ")");
        	$.unloading();
			if(XMLHttpRequest.status == 403){
				$.messaging("您没有该权限！","false",5000);
				if($(".panel-loading").length > 0){
					var forbiden = $("<div>").addClass("forbidenDiv")
					.append($("<p>").addClass("forbidenP").append($("<img>").attr("src","resources/themes/img/forbidden.jpg"))) 
					.append($("<p>").addClass("forbidenP").html("您没有该权限!"));
					$(".panel-loading").removeClass("panel-loading").html(forbiden);
				}
			}else if(XMLHttpRequest.status == 800){//session kick out status
				$.messaging("您已经在其它地方登陆,5秒后返回登录页！","false",5000);
				setTimeout(function(){
					location.href = "login.html?status=800";
				}, 5000);
				if($(".panel-loading").length > 0){
					var forbiden = $("<div>").addClass("forbidenDiv")
					.append($("<p>").addClass("forbidenP").append($("<img>").attr("src","resources/themes/img/forbidden.jpg"))) 
					.append($("<p>").addClass("forbidenP").html("您已经在其它地方登陆,5秒后返回登录页"));
					$(".panel-loading").removeClass("panel-loading").html(forbiden);
				}
			}else if(XMLHttpRequest.status == 801){//session timeout status
				$.messaging("会话已经过期,5秒后返回登录页！","false",5000);
				setTimeout(function(){
					location.href = "login.html?status=801";
				}, 5000);
				if($(".panel-loading").length > 0){
					var forbiden = $("<div>").addClass("forbidenDiv")
					.append($("<p>").addClass("forbidenP").append($("<img>").attr("src","resources/themes/img/forbidden.jpg"))) 
					.append($("<p>").addClass("forbidenP").html("会话已经过期,5秒后返回登录页!"));
					$(".panel-loading").removeClass("panel-loading").html(forbiden);
				}
			}else if(XMLHttpRequest.status == 803){//user not login but want to access resources
				$.messaging("您还没有登录,5秒后返回登录页！","false",5000);
				setTimeout(function(){
					location.href = "login.html?status=801";
				}, 5000);
				if($(".panel-loading").length > 0){
					var forbiden = $("<div>").addClass("forbidenDiv")
					.append($("<p>").addClass("forbidenP").append($("<img>").attr("src","resources/themes/img/forbidden.jpg"))) 
					.append($("<p>").addClass("forbidenP").html("会话已经过期,5秒后返回登录页!"));
					$(".panel-loading").removeClass("panel-loading").html(forbiden);
				}
			}else if(XMLHttpRequest.status == 500){//server internal error
				$.messaging("系统错误","false",5000);
				if($(".panel-loading").length > 0){
					var forbiden = $("<div>").addClass("forbidenDiv")
					.append($("<p>").addClass("forbidenP").append($("<img>").attr("src","resources/themes/img/forbidden.jpg"))) 
					.append($("<p>").addClass("forbidenP").html("系统错误!"));
					$(".panel-loading").removeClass("panel-loading").html(forbiden);
				}
			}
		
			
            //old(xhr,status,errMsg||err);  
			if(old != undefined){
				old(XMLHttpRequest, textStatus, errorThrown);
			}
        };
        ajax(s);  
    };
  
})(jQuery);   