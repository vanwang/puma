<!DOCTYPE html>  
<html>  
<head>  
    <meta charset="UTF-8">  
    <title>PUMA Framework&mdash;&mdash;让系统开发更简单一点</title>  
    <link rel="stylesheet" type="text/css" href="resources/themes/icon2.css">
    <link rel="stylesheet" type="text/css" href="resources/themes/main.css">  
    <script type="text/javascript" src="resources/jquery.js"></script>  
    <script type="text/javascript" src="resources/easyloader.js"></script>
</head>  
<body>
	<a id="open-menu" href="javascript:void(0);"><span>Menu</span></a>
	<div id="frame-top" class="frame-top">
		<div class="link-container">
			<!-- <h1 id="logo-id" class="logo">PUMA <span style="font-size:12px;">FRAMEWORK</span></h1> -->
			<img alt="puma logo" id="logo-image-id" class="logo" width="180px" src="resources/themes/img/puma-logo-single.png">
			<ul class="link-ul">
				<li class="account-li">
					<a href="#" class="easyui-menubutton top-text">${(member.email)!""}
					</a>
				</li>
				<li class="seperator-li">
					|
				</li>
				<li class="link-item-li">
					<a class="easyui-linkbutton link-item-a top-text" data-options="plain:true" href="#module=UserManagementModule|{}">系统设置</a>
				</li>
				<li class="seperator-li">
					|
				</li>
				<li class="link-item-li">
					<a class="easyui-linkbutton link-item-a top-text" data-options="plain:true" href="#module=ChangeSkinModule|{}">换肤</a>
				</li>
				<li class="seperator-li">
					|
				</li>
				<li class="link-item-li">
					<a class="easyui-linkbutton link-item-a top-text" data-options="plain:true" href="doc/index.html" target="_blank">文档
					</a>
				</li>
				<!-- <li class="link-item-li">
					<a class="easyui-linkbutton link-item-a top-text" data-options="plain:true" href="javascript:void(0)" onclick="javascript:$.messaging('aaa',true);$.messaging('bbbb',true)">message
					</a>
				</li> -->
				<li class="seperator-li">
					|
				</li>
				<li class="link-item-li">
					<a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-sign-out'" href="javascript:location.href='j_spring_security_logout';">退出</a>
				</li>
<!-- 				<li class="seperator-li">
					|
				</li>
				<li class="link-item-li">
					<a class="easyui-linkbutton link-item-a top-text" data-options="plain:true" onclick="javascript:$('#main-tab').tabs('select',1)">选择</a>
				</li>
 -->			</ul>
		</div>
	</div>
	<div class="frame-main">
		<div class="frame-main-nav">
			<div id="frame-main-nav" class="easyui-panel" data-options="fit:true,noheader:true,border:false"></div>
		</div>
		<div class="frame-main-content">
			<div id="main-tab" class="easyui-tabs" data-options="fit:true,plain:true"></div>
		</div>
	</div>
	<div id="skin" class="skin">
		<div class="skin-item skin-top"></div>
		<div class="skin-item skin-top-left"></div>
		<div class="skin-item skin-top-right"></div>
		<div class="skin-item skin-left"></div>
		<div class="skin-item skin-left-top"></div>
		<div class="skin-item skin-right"></div>
		<div class="skin-item skin-right-hack"></div>
		<div class="skin-item skin-bottom"></div>
		<div class="skin-item skin-bottom-left"></div>
		<div class="skin-item skin-bottom-right"></div>
	</div>
</body>  
</html>  